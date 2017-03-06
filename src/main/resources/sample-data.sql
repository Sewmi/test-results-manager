
DROP TABLE IF EXISTS `integration_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `integration_tests` (
  `product` varchar(220) NOT NULL,
  `version` varchar(50) NOT NULL,
  `buildNo` int(10) NOT NULL,
  `platform` text NOT NULL,
  `test` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `unit_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit_tests` (
  `component` varchar(220) NOT NULL,
  `version` varchar(50) NOT NULL,
  `buildNo` int(10) NOT NULL,
  `test` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


-- Unit test results --

INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent1', '9.9', 100, 'org.wso2.am.unit.compenent1.Class1#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent1', '9.9', 100, 'org.wso2.am.unit.compenent1.Class1#method2', 'FAIL');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent1', '9.9', 100, 'org.wso2.am.unit.compenent1.Class1#method3', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class1#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class1#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class2#method1', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class2#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class3#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class3#method2', 'FAIL');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class4#method1', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent2', '1.0.0', 100, 'org.wso2.am.unit.compenent2.Class4#method2', 'FAIL');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class1#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class1#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class2#method1', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class2#method2', 'FAIL');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class3#method1', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class3#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class4#method1', 'SKIP');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class4#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class5#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class5#method2', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class6#method1', 'PASS');
INSERT INTO unit_tests (component, version, buildNo, test, status ) VALUES ('org.wso2.am.compenent3', '2.0.0', 100, 'org.wso2.am.unit.compenent3.Class6#method2', 'PASS');

-- Integration test results --

INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class1#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class1#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class2#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class2#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class3#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class3#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class4#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class4#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class5#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class5#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class6#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class6#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class7#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class7#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class8#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class8#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class9#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class9#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class10#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class10#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class11#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class11#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class12#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class12#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class13#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class13#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class14#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class14#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class15#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class15#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class16#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class16#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class17#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class17#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class18#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class18#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class19#method1', 'SKIP');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class19#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class20#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class20#method1', 'PASS');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT1', 'org.wso2.am.integration.Class21#method1', 'FAIL');
INSERT INTO integration_tests (product, version, buildNo, platform , test, status ) VALUES ('APIM', '3.0.0', 1001, 'PT2', 'org.wso2.am.integration.Class21#method1', 'PASS');
