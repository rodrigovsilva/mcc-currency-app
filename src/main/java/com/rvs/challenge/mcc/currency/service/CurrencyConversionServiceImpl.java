package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.dto.ExchangeRateDTO;
import com.rvs.challenge.mcc.currency.exception.ConversionRatesException;
import com.rvs.challenge.mcc.currency.model.CurrencyConversion;
import com.rvs.challenge.mcc.currency.repository.CurrencyConversionRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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

    @Override
    public CurrencyConversionDTO getConversionRates(CurrencyConversionDTO currencyConversionData) {

        LOGGER.info("getConversionRates: {}", ObjectParserUtil.getInstance().toString(currencyConversionData));
        MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
        uriVariables.add("access_key", env.getProperty(Constants.CURRENCY_API_KEY));
        //uriVariables.add("currencies", target.getCode());
        uriVariables.add("source", currencyConversionData.getExchange());
        uriVariables.add("format", "1");

        //http://www.mocky.io/v2/5b199e6d3000005a00da17c7
        /*UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(env.getProperty(Constants.CURRENCY_API_BASE_URL))
                .queryParams(uriVariables).build();*/

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://www.mocky.io/v2/5b23469c2f00006a00e09483")
                .queryParams(uriVariables).build();

        RestTemplate restTemplate = new RestTemplate();
        ConversionRates conversionRates = restTemplate.getForObject(uriComponents.toUri(), ConversionRates.class);

        LOGGER.info("getConversionRates: conversionRates {}", ObjectParserUtil.getInstance().toString(conversionRates));

        if (conversionRates.getSuccess()) {
            Calendar timestamp = Calendar.getInstance();
            timestamp.setTimeInMillis(conversionRates.getTimestamp());
            currencyConversionData.setTimestamp(timestamp);

            if (conversionRates.getQuotes() != null) {
                List<ExchangeRateDTO> exchangeRates = conversionRates.getQuotes().entrySet().stream()
                        .map(e -> new ExchangeRateDTO(
                                StringUtils.substringAfter(e.getKey(), conversionRates.getSource()), e.getValue()))
                        .collect(Collectors.toList());
                currencyConversionData.setExchangeRates(exchangeRates);
            }

        } else {
            throw new ConversionRatesException(conversionRates.getError().getInfo());
        }

        LOGGER.info("getConversionRates: currencyConversionData {}", ObjectParserUtil.getInstance().toString(currencyConversionData));


        return currencyConversionData;
    }

    @Override
    public List<CurrencyConversionDTO> getHistoricalCurrencyConversions(int listSize) {
        Pageable pageable = new PageRequest(0, listSize, Sort.Direction.DESC, "timestamp");

        Optional<Page<CurrencyConversion>> currencyConversions = currencyConversionRepository.findAll(pageable);

        // this is a list of the last 10 records, you can choose to invert it by using
        //List<CurrencyConversion> bottomUsersList = bottomPage.getContent();

        //Collections.inverse(currencyConversions);
        return currencyConversions.isPresent()
                ? currencyConversions.get().getContent()
                .parallelStream()
                .map(c -> new CurrencyConversionDTO(c.getExchange(), c.getTimestamp(),
                        Optional.ofNullable(c.getExchangeRates()).isPresent()
                                ? c.getExchangeRates().parallelStream()
                                .map(er -> new ExchangeRateDTO(er.getExchange(), er.getRate())).collect(Collectors.toList())
                                : new ArrayList<>())).collect(Collectors.toList())
                : new ArrayList<>();
    }
}
