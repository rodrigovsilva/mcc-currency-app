package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.exception.ConversionRatesException;
import com.rvs.challenge.mcc.currency.model.CurrencyConversion;
import com.rvs.challenge.mcc.currency.model.ExchangeRate;
import com.rvs.challenge.mcc.currency.model.User;
import com.rvs.challenge.mcc.currency.repository.CurrencyConversionRepository;
import com.rvs.challenge.mcc.currency.repository.UserRepository;
import com.rvs.challenge.mcc.currency.service.currencyconverter.ConversionRates;
import com.rvs.challenge.mcc.currency.util.Constants;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.lang.invoke.MethodHandles;
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

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    public CurrencyConversionDTO convert(CurrencyConversionDTO currencyConversionData) {

        Optional<User> searchedUser = userRepository.findByUsername(securityService.findLoggedInUsername());

        if(searchedUser.isPresent()) {

            LOGGER.info("convert: {} {}", currencyConversionData.getTimestamp().toGMTString(), ObjectParserUtil.getInstance().toString(currencyConversionData));

            MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
            uriVariables.add("access_key", env.getProperty(Constants.CURRENCY_API_KEY));
            uriVariables.add("currencies", currencyConversionData.getExchangeFrom());
            uriVariables.add("source", currencyConversionData.getExchangeFrom());
            uriVariables.add("timestamp", currencyConversionData.getTimestamp().toString());
            uriVariables.add("format", "1");

            //http://www.mocky.io/v2/5b199e6d3000005a00da17c7
        /*UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(env.getProperty(Constants.CURRENCY_API_BASE_URL))
                .queryParams(uriVariables).build();*/

            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://www.mocky.io/v2/5b23469c2f00006a00e09483")
                    .queryParams(uriVariables).build();

            RestTemplate restTemplate = new RestTemplate();
            ConversionRates conversionRates = restTemplate.getForObject(uriComponents.toUri(), ConversionRates.class);

            LOGGER.info("convert: conversionRates {}", ObjectParserUtil.getInstance().toString(conversionRates));

            if (conversionRates.getSuccess()) {

                // updating rates timestamp
                Calendar timestamp = Calendar.getInstance();
                timestamp.setTimeInMillis(conversionRates.getTimestamp());

                currencyConversionData.setTimestamp(timestamp.getTime());

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

                CurrencyConversion currencyConversionToSave = new CurrencyConversion(currencyConversionData.getExchangeFrom(), currencyConversionData.getExchangeTo(), timestamp, currencyConversionData.getRate(), searchedUser.get());

                LOGGER.info("convert: currencyConversionToSave {}", ObjectParserUtil.getInstance().toString(currencyConversionToSave.getUser().getUsername()));

                currencyConversionRepository.save(currencyConversionToSave);

            } else {
                throw new ConversionRatesException(conversionRates.getError().getInfo());
            }
        } else {
            throw new UsernameNotFoundException("There is no user logged in or registered on database.");
        }

        return currencyConversionData;
    }

    @Override
    public List<CurrencyConversionDTO> getHistoricalCurrencyConversions(int listSize) {

        String username = securityService.findLoggedInUsername();

        Optional<User> searchedUser = userRepository.findByUsername(username);

        if(searchedUser.isPresent()) {
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
}
