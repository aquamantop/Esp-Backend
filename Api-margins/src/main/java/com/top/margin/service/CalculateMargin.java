package com.top.margin.service;

import com.top.margin.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.top.margin.model.RiskLevel.*;

@Service
public class CalculateMargin {

    private List<Risk> riskList = new ArrayList<>();

    @PostConstruct
    public void init() {

        riskList.add(new Risk(EconomicActivity.ForestProduction, LIGHT, new BigDecimal(300000)));
        riskList.add(new Risk(EconomicActivity.Fishing, LIGHT, new BigDecimal(301000)));
        riskList.add(new Risk(EconomicActivity.Agriculture, LIGHT, new BigDecimal(302000)));
        riskList.add(new Risk(EconomicActivity.Industry, LIGHT, new BigDecimal(303000)));
        riskList.add(new Risk(EconomicActivity.Trade, LIGHT, new BigDecimal(304000)));
        riskList.add(new Risk(EconomicActivity.Tourism, LIGHT, new BigDecimal(305000)));
        riskList.add(new Risk(EconomicActivity.Bank, LIGHT, new BigDecimal(306000)));
        riskList.add(new Risk(EconomicActivity.Education, LIGHT, new BigDecimal(307000)));
        riskList.add(new Risk(EconomicActivity.ProductionSupportServices, LIGHT, new BigDecimal(308000)));
        riskList.add(new Risk(EconomicActivity.CattleRaising, LIGHT, new BigDecimal(309000)));

        riskList.add(new Risk(EconomicActivity.ForestProduction, MEDIUM, new BigDecimal(200000)));
        riskList.add(new Risk(EconomicActivity.Fishing, MEDIUM, new BigDecimal(201000)));
        riskList.add(new Risk(EconomicActivity.Agriculture, MEDIUM, new BigDecimal(202000)));
        riskList.add(new Risk(EconomicActivity.Industry, MEDIUM, new BigDecimal(203000)));
        riskList.add(new Risk(EconomicActivity.Trade, MEDIUM, new BigDecimal(204000)));
        riskList.add(new Risk(EconomicActivity.Tourism, MEDIUM, new BigDecimal(205000)));
        riskList.add(new Risk(EconomicActivity.Bank, MEDIUM, new BigDecimal(206000)));
        riskList.add(new Risk(EconomicActivity.Education, MEDIUM, new BigDecimal(207000)));
        riskList.add(new Risk(EconomicActivity.ProductionSupportServices, MEDIUM, new BigDecimal(208000)));
        riskList.add(new Risk(EconomicActivity.CattleRaising, MEDIUM, new BigDecimal(209000)));

        riskList.add(new Risk(EconomicActivity.ForestProduction, HIGH, new BigDecimal(100000)));
        riskList.add(new Risk(EconomicActivity.Fishing, HIGH, new BigDecimal(101000)));
        riskList.add(new Risk(EconomicActivity.Agriculture, HIGH, new BigDecimal(102000)));
        riskList.add(new Risk(EconomicActivity.Industry, HIGH, new BigDecimal(103000)));
        riskList.add(new Risk(EconomicActivity.Trade, HIGH, new BigDecimal(104000)));
        riskList.add(new Risk(EconomicActivity.Tourism, HIGH, new BigDecimal(105000)));
        riskList.add(new Risk(EconomicActivity.Bank, HIGH, new BigDecimal(106000)));
        riskList.add(new Risk(EconomicActivity.Education, HIGH, new BigDecimal(107000)));
        riskList.add(new Risk(EconomicActivity.ProductionSupportServices, HIGH, new BigDecimal(108000)));
        riskList.add(new Risk(EconomicActivity.CattleRaising, HIGH, new BigDecimal(109000)));

    }

    public Calification calculate(String type, String documentValue) {
        int economicActivity = Integer.parseInt(documentValue.charAt(documentValue.length() - 1) + "");
        int risk = Integer.parseInt(documentValue.charAt(documentValue.length() - 2) + "");
        int numVerificator = Integer.parseInt(documentValue.charAt(documentValue.length() - 3) + "");
        RiskLevel riskName = risk <= 3 ? LIGHT : risk <= 7 ? MEDIUM : HIGH;
        BigDecimal marginTotal = riskList.stream().filter(risk1 ->
                risk1.getRiskLevel().equals(riskName)
                        && risk1.getEconomicActivity().ordinal() == economicActivity
        ).findFirst().get().getMargin();


        Calification calification = new Calification();
        calification.setCustomerCategory(economicActivity == risk && risk == numVerificator ? CustomerCategory.PREMIUM : CustomerCategory.BASIC);
        calification.setDocumentType(type);
        calification.setDocumentValue(documentValue);
        calification.setRiskLevel(riskName);
        calification.setTotalMargin(marginTotal);
        calification.setTotalMarginAdditional(calification.getCustomerCategory().equals(CustomerCategory.PREMIUM) ? new BigDecimal(10000000) : BigDecimal.ZERO);
        calification.setEconomicActivity(Arrays.stream(EconomicActivity.values()).toList().get(economicActivity));
        calification.setSublimits(new ArrayList<>());

        marginTotal=calification.getCustomerCategory().equals(CustomerCategory.PREMIUM) ? marginTotal.add(new BigDecimal(10000000)) : marginTotal;

        var cardSublimit = new Calification.Sublimit();
        cardSublimit.setConcept(Concept.CARD);
        cardSublimit.setTotalMargin(marginTotal.multiply(new BigDecimal(60)).divide(new BigDecimal(100), 2, RoundingMode.CEILING));
        calification.getSublimits().add(cardSublimit);

        var checkSublimit = new Calification.Sublimit();
        checkSublimit.setConcept(Concept.CHECK);
        checkSublimit.setTotalMargin(marginTotal.multiply(new BigDecimal(10)).divide(new BigDecimal(100), 2, RoundingMode.CEILING));
        calification.getSublimits().add(checkSublimit);

        var loanSublimit = new Calification.Sublimit();
        loanSublimit.setConcept(Concept.LOAN);
        loanSublimit.setTotalMargin(marginTotal.multiply(new BigDecimal(30)).divide(new BigDecimal(100), 2, RoundingMode.CEILING));
        calification.getSublimits().add(loanSublimit);
        return calification;
    }
}
