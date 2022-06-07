/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.producingwebservice;

import com.eviware.soapui.tools.SoapUITestCaseRunner;
import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProducingWebServiceApplicationIntegrationTests {

	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port = 0;

	@BeforeEach
	public void init() throws Exception {
		marshaller.setPackagesToScan(ClassUtils.getPackageName(GetCountryRequest.class));
		marshaller.afterPropertiesSet();
	}

	@Test
	public void testSendAndReceive() {
		WebServiceTemplate ws = new WebServiceTemplate(marshaller);
		GetCountryRequest request = new GetCountryRequest();
		request.setName("Spain");

		assertThat(ws.marshalSendAndReceive("http://localhost:"
				+ port + "/ws", request) != null);
    }

	@Test
	public void soapUiProjectTest() throws Exception {
		SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
		runner.setProjectFile( "doc/countries-soapui-project.xml" );
		runner.run();

/*
		WsdlProject project = new WsdlProject( "doc/countries-soapui-project.xml" );
		TestSuite testSuite = project.getTestSuiteByName( "Test Countries Suite" );
		TestCase testCase = testSuite.getTestCaseByName( "Test getCountry" );

		// create empty properties and run synchronously
		TestRunner runner = testCase.run( new PropertiesMap(), false );
		assertEquals( TestRunner.Status.FINISHED, runner.getStatus() );*/
	}
}
