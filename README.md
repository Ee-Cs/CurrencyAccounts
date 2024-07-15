# Currency Accounts
<p>
This is the Spring Boot application with HyperSQL database. The <a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/docs/mermaid/flowchart.md">flowchart diagram</a>
with endpoints.
</p>
<hr>
<h3>❶ The Java classes</h3>
<p>
The account:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/domain/Account.java#L17">
kp.domain.Account</a>.<br/>
</p>
<p>
The account creation endpoint logic.<br/>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L41">
kp.controller.KpController::createAccount</a>.<br/>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L46">
kp.services.KpService::createAccount</a>.
</p>
<p>
The exchange currency endpoint logic.<br/>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L58">
kp.controller.KpController::exchangeCurrency</a>.<br/>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L70">
kp.services.KpService::exchangeCurrency</a>.
</p>

<p>
The account statement endpoint logic.<br/>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L72">
kp.controller.KpController::getAccountStatement</a>.<br/>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L108">
kp.services.KpService::getAccountStatement</a>.
</p>
<p>
The National Bank client service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/ExchangeRateService.java#L38">
kp.services.ExchangeRateService::getExchangeRate</a>.<br/>
The National Bank <a href="http://api.nbp.pl/api/exchangerates/rates/a/usd/">
exchange rates endpoint</a> used in this application. 
</p>
<hr>
<h3>❷ The tests</h3>
<p>
<span style="font-size:2.5em">𝓐.</span> The <a href="https://github.com/Ee-Cs/CurrencyAccounts/tree/main/src/test/java/kp">
test classes</a>. They use JUnit Jupiter and MockMvc.
</p>
<p>
<span style="font-size:1.5em">𝓑.</span> The screenshot of the 20 application tests executed in IntelliJ IDEA:<br/>
<img alt="" src="docs/images/ScreenshotTests.png"/>
</p>
<p>
<span style="font-size:1.5em">𝓒.</span> The endpoint tests in Postman.
The exported <a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/docs/postman/3%20Currency%20Accounts.postman_collection.json">
Postman collection</a>.
</p>
<ol>The test scenario:
<li>create the account</li>
<li>get account statement</li>
<li>exchange currency PLN → USD</li>
<li>get account statement</li>
<li>exchange currency USD → PLN</li>
<li>get account statement</li>
</ol>
<p>
The screenshots of this test scenario executed in Postman. 
<p>
1. Creating the account:<br/>
<img alt="" src="docs/images/ScreenshotCreateAccount.png"/>
</p>
<p>
2. The account statement after creation:<br/>
<img alt="" src="docs/images/ScreenshotAccountStatement1.png"/>
</p>
<p>
3. The currency exchange from local (PLN) to foreign (USD):<br/>
<img alt="" src="docs/images/ScreenshotExchangeFromPLNToUSD.png"/>
</p>
<p>
4. The account statement after exchange from PLN to USD:<br/>
<img alt="" src="docs/images/ScreenshotAccountStatement2.png"/>
</p>
<p>
5. The currency exchange from foreign (USD) to local (PLN):<br/>
<img alt="" src="docs/images/ScreenshotExchangeFromUSDToPLN.png"/>
</p>
<p>
6. The account statement after exchange from USD to PLN:<br/>
<img alt="" src="docs/images/ScreenshotAccountStatement3.png"/>
</p>
<p>
As it is presented on the above screenshot, after these two transactions (PLN → USD and USD → PLN)
the account balance is not different from original (of course with small roundings).  
</p>
<p>
<span style="font-size:1.5em">𝓓.</span> The screenshots of the application responses in the Firefox Browser.<br/>
</p>
<p>
The account statement before the account creation:<br/>
<img alt="" src="docs/images/ScreenshotAccountStatementBeforeAccountCreation.png"/>
</p>
<p>
The account statement after the account creation:<br/>
<img alt="" src="docs/images/ScreenshotAccountStatementAfterAccountCreation.png"/><br/>
</p>
<hr>
<h3>❸ The documentation</h3>
<p>
<a href="https://htmlpreview.github.io/?https://github.com/Ee-Cs/CurrencyAccounts/blob/main/docs/apidocs/index.html">
Java API Documentation</a>
</p>
<hr/>
<h3>❹ The future design ideas</h3>
<ul>
<li>add authentication and authorization</li>
<li>extend account data with dates and add more personal details (like address)</li>
<li>refactor database schema</li>
<li>switch to PostgreSQL database</li>
<li>save exchange receipts to database</li> 
<li>add endpoint for account deleting</li>
<li>add endpoints for more account transactions types (like money withdrawing etc.)</li>
<li>add endpoints for customer activity reporting</li>
<li>maybe it would be needed the foreign exchange commission or other fees</li>
<li>generate the endpoint from OpenApi yaml file, use Swagger</li>
<li>move to Docker, or Kubernetes, or cloud (AWS, MS Azure or Google Cloud Platform)</li>
<li>after refactoring add more tests</li>
</ul>
<hr/>