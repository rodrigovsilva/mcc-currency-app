package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.exception.ConversionRatesException;
import com.rvs.challenge.mcc.currency.model.CurrencyConversion;
import com.rvs.challenge.mcc.currency.model.ExchangeRate;
import com.rvs.challenge.mcc.currency.model.User;
import com.rvs.challenge.mcc.currency.repository.CurrencyConversionRepository;
import com.rvs.challenge.mcc.currency.repository.UserRepository;
import com.rvs.challenge.mcc.currency.service.currencyconverter.AvailableCurrencies;
import com.rvs.challenge.mcc.currency.service.currencyconverter.ConversionRates;
import com.rvs.challenge.mcc.currency.util.Constants;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import sun.plugin2.message.Message;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of all currency converter services.
 */
@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Date format to currency api.
     */
    private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CurrencyConversionDTO convert(CurrencyConversionDTO currencyConversionData) {

        Optional<User> searchedUser = userRepository.findByUsername(securityService.findLoggedInUsername());

        if (searchedUser.isPresent()) {

            MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
            uriVariables.add("access_key", env.getProperty(Constants.CURRENCY_API_KEY));
            uriVariables.add("source", currencyConversionData.getExchangeFrom());
            uriVariables.add("format", "1");

            // timestamp comparison to define live or historical service
            int timestampComparison = DateTimeComparator.getDateOnlyInstance().compare(currencyConversionData.getTimestamp(), Calendar.getInstance().getTime());

            String currencyApiUri;

            if (timestampComparison >= 0) {
                currencyApiUri = env.getProperty(Constants.CURRENCY_API_URI_LIVE);
            } else {
                currencyApiUri = env.getProperty(Constants.CURRENCY_API_URI_HISTORICAL);
                uriVariables.add("date", dateFormat.format(currencyConversionData.getTimestamp()));
            }

            LOGGER.info("uriVariables {} {}", currencyApiUri, ObjectParserUtil.getInstance().toString(uriVariables));

            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(currencyApiUri).queryParams(uriVariables).build();

            RestTemplate restTemplate = new RestTemplate();
            ConversionRates conversionRates = restTemplate.getForObject(uriComponents.toUri(), ConversionRates.class);

            LOGGER.info("convert: conversionRates {}", ObjectParserUtil.getInstance().toString(conversionRates));

            if (conversionRates.getSuccess()) {

                // if there is quotes from results
                if (conversionRates.getQuotes() != null) {

                    // get all exchange rates to cache
                    Set<ExchangeRate> exchangeRates = conversionRates.getQuotes().entrySet().stream()
                            .map(e -> new ExchangeRate(
                                    StringUtils.substringAfter(e.getKey(), conversionRates.getSource()), e.getValue()))
                            .collect(Collectors.toSet());

                    // filter the exchange to conversion rate
                    Set<ExchangeRate> filteredRates = exchangeRates.stream()
                            .filter(er -> StringUtils.equalsIgnoreCase(er.getExchange(), currencyConversionData.getExchangeTo())).collect(Collectors.toSet());

                    // if there is rate, update it on currency conversion object
                    if (filteredRates != null) {
                        currencyConversionData.setRate(filteredRates.iterator().next().getRate());
                    }

                }

                // update timestamp
                Calendar timestamp =Calendar.getInstance();
                timestamp.setTime(currencyConversionData.getTimestamp());

                CurrencyConversion currencyConversionToSave = new CurrencyConversion(currencyConversionData.getExchangeFrom(), currencyConversionData.getExchangeTo(), timestamp, currencyConversionData.getRate(), searchedUser.get());

                LOGGER.info("convert: currencyConversionToSave {}", ObjectParserUtil.getInstance().toString(currencyConversionToSave.getUser().getUsername()));

                currencyConversionRepository.save(currencyConversionToSave);

            } else {
                throw new ConversionRatesException(messageSource.getMessage("Exception.currency.api.error", null, Locale.getDefault()));
            }
        } else {
            throw new UsernameNotFoundException(messageSource.getMessage("Exception.user.not.exists.error", null, Locale.getDefault()));
        }

        return currencyConversionData;
    }

    @Override
    public List<CurrencyConversionDTO> getHistoricalCurrencyConversions(int listSize) {

        String username = securityService.findLoggedInUsername();

        Optional<User> searchedUser = userRepository.findByUsername(username);

        if (searchedUser.isPresent()) {
            Pageable pageable = new PageRequest(0, listSize, Sort.Direction.DESC, "createdAt");

            Optional<Page<CurrencyConversion>> currencyConversions = currencyConversionRepository.findAllByUser(searchedUser.get(), pageable);

            // serialize conversion model list to dto list
            return currencyConversions.isPresent()
                    ? currencyConversions.get().getContent()
                    .parallelStream()
                    .map(c -> new CurrencyConversionDTO(c.getExchangeFrom(), c.getTimestamp().getTime(),
                            c.getExchangeTo(), c.getRate(), c.getCreatedAt())).collect(Collectors.toList())
                    : new ArrayList<>();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Cacheable("cacheAvailableCurrencies")
    public Set<String> getAvailableCurrencies() {

        MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
        uriVariables.add("access_key", env.getProperty(Constants.CURRENCY_API_KEY));

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(env.getProperty(Constants.CURRENCY_API_URI_AVAILABLE_CURRENCIES))
                .queryParams(uriVariables).build();

        RestTemplate restTemplate = new RestTemplate();
        AvailableCurrencies availableCurrencies = restTemplate.getForObject(uriComponents.toUri(), AvailableCurrencies.class);

        List<String> currencies = new ArrayList<>();

        if (availableCurrencies != null && availableCurrencies.isSuccess()) {
            currencies = availableCurrencies.getCurrencies().entrySet().stream()
                    .map(e -> e.getKey()).sorted(String::compareTo)
                    .collect(Collectors.toList());
        } else {
            throw new ConversionRatesException(messageSource.getMessage("Exception.currency.api.error", null, Locale.getDefault()));

        }

        LOGGER.info("availableCurrencies {}", ObjectParserUtil.getInstance().toString(availableCurrencies));

        return new LinkedHashSet<>(currencies);
    }


}
