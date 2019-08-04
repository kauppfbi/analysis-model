package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import edu.hm.hafner.util.IntegerParser;

/**
 * The AnalyzerType for PVS-Studio static analyzer.
 *
 * @author PVS-Studio Team
 */
public final class AnalyzerType {
    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    private static final int VIVA64_CCPP_ERRORCODE_BEGIN = 100;
    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    private static final int VIVA64_CCPP_ERRORCODE_END = 499;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    private static final int GENERAL_CCPP_LOW_ERRORCODE_BEGIN = 500;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    private static final int GENERAL_CCPP_LOW_ERRORCODE_END = 799;

    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    private static final int OPTIMIZATION_CCPP_ERRORCODE_BEGIN = 800;
    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    private static final int OPTIMIZATION_CCPP_ERRORCODE_END = 999;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    private static final int GENERAL_CCPP_HIGH_ERRORCODE_BEGIN = 1000;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    private static final int GENERAL_CCPP_HIGH_ERRORCODE_END = 1999;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN = 2000;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_END = 2499;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    private static final int MISRA_CCPP_ERRORCODE_BEGIN = 2500;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    private static final int MISRA_CCPP_ERRORCODE_END = 2999;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    private static final int GENERAL_CS_ERRORCODE_BEGIN = 3000;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    private static final int GENERAL_CS_ERRORCODE_END = 3499;
    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    private static final int GENERAL_JAVA_ERRORCODE_BEGIN = 6000;
    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    private static final int GENERAL_JAVA_ERRORCODE_END = 6999;

    /**
     * Viva64 Issue Type.
     */
    public static final String VIVA_64_MESSAGE = "64-bit";
    /**
     * General Issue Type.
     */
    public static final String GENERAL_MESSAGE = "General Analysis";
    /**
     * Micro-optimization Issue Type.
     */
    public static final String OPTIMIZATION_MESSAGE = "Micro-optimization";
    /**
     * Specific Requests Issue Type.
     */
    public static final String CUSTOMER_SPECIFIC_MESSAGE = "Specific Requests";
    /**
     * MISRA Issue Type.
     */
    public static final String MISRA_MESSAGE = "MISRA";
    /**
     * Unknown Issue Type.
     */
    public static final String UNKNOWN_MESSAGE = "Unknown";

    private AnalyzerType() {

    }

    private static List<AnalysisType> analysisTypes = new ArrayList<>();
    static {
        analysisTypes.add(new Viva64());
        analysisTypes.add(new GENERAL());
        analysisTypes.add(new OPTIMIZATION());
        analysisTypes.add(new CustomerSpecific());
        analysisTypes.add(new MISRA());
    }

    /**
     * errorCodeStr format is Vnnn.
     */
    static AnalysisType fromErrorCode(final String errorCodeStr) {

        if ("External".equalsIgnoreCase(errorCodeStr)) {
            return new GENERAL();
        }

        int errorCode = IntegerParser.parseInt(errorCodeStr.substring(1));

        return analysisTypes.stream().map(type -> type.create(errorCode)).flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .findFirst()
                .orElse(new UNKNOWN());
    }

    /**
     * Viva64 AnalysisType.
     */
    static final class Viva64 implements AnalysisType {

        private Viva64() {

        }

        @Override
        public String getMessage() {
            return VIVA_64_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= VIVA64_CCPP_ERRORCODE_BEGIN && errorCode <= VIVA64_CCPP_ERRORCODE_END) {
                return Optional.of(new Viva64());
            }

            return Optional.empty();
        }
    }

    /**
     * GENERAL AnalysisType.
     */
    static final class GENERAL implements AnalysisType {

        private GENERAL() {

        }

        @Override
        public String getMessage() {
            return GENERAL_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= GENERAL_CCPP_LOW_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_LOW_ERRORCODE_END) {
                return Optional.of(new GENERAL());
            }
            else if (errorCode >= GENERAL_CCPP_HIGH_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_HIGH_ERRORCODE_END) {
                return Optional.of(new GENERAL());
            }
            else if (errorCode >= GENERAL_CS_ERRORCODE_BEGIN && errorCode <= GENERAL_CS_ERRORCODE_END) {
                return Optional.of(new GENERAL());
            }
            else if (errorCode >= GENERAL_JAVA_ERRORCODE_BEGIN && errorCode <= GENERAL_JAVA_ERRORCODE_END) {
                return Optional.of(new GENERAL());
            }
            else {
                return Optional.empty();
            }
        }
    }

    /**
     * OPTIMIZATION AnalysisType.
     */
    static final class OPTIMIZATION implements AnalysisType {

        private OPTIMIZATION() {

        }

        @Override
        public String getMessage() {
            return OPTIMIZATION_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= OPTIMIZATION_CCPP_ERRORCODE_BEGIN && errorCode <= OPTIMIZATION_CCPP_ERRORCODE_END) {
                return Optional.of(new OPTIMIZATION());
            }

            return Optional.empty();
        }
    }

    /**
     * CustomerSpecific AnalysisType.
     */
    static final class CustomerSpecific implements AnalysisType {

        private CustomerSpecific() {

        }

        @Override
        public String getMessage() {
            return CUSTOMER_SPECIFIC_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN && errorCode <= CUSTOMERSPECIFIC_CCPP_ERRORCODE_END) {
                return Optional.of(new CustomerSpecific());
            }

            return Optional.empty();
        }
    }

    /**
     * MISRA AnalysisType.
     */
    static final class MISRA implements AnalysisType {

        private MISRA() {

        }

        @Override
        public String getMessage() {
            return MISRA_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= MISRA_CCPP_ERRORCODE_BEGIN && errorCode <= MISRA_CCPP_ERRORCODE_END) {
                return Optional.of(new MISRA());
            }

            return Optional.empty();
        }
    }

    /**
     * Unkonwn AnalysisType.
     */
    static final class UNKNOWN implements AnalysisType {

        private UNKNOWN() {

        }

        @Override
        public String getMessage() {
            return UNKNOWN_MESSAGE;
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {
            return Optional.empty();
        }
    }
}