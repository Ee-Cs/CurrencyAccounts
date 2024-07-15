# CurrencyAccounts
<p>
The <a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/docs/mermaid/flowchart.md">flowchart diagram</a>
with endpoints.
</p>
<hr>
<h3>❶ The Java classes</h3>
<h3>The account creation endpoint logic</h3>
<p>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L41">
kp.controller.KpController::createAccount</a>.
</p>
<p>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L46">
kp.services.KpService::createAccount</a>.
</p>
<h3>The exchange currency endpoint logic</h3>
<p>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L58">
kp.controller.KpController::exchangeCurrency</a>.
</p>
<p>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L70">
kp.services.KpService::exchangeCurrency</a>.
</p>
<h3>The account statement endpoint logic</h3>
<p>
The controller method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/controller/KpController.java#L72">
kp.controller.KpController::getAccountStatement</a>.
</p>
<p>
The service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/KpService.java#L108">
kp.services.KpService::getAccountStatement</a>.
</p>
<p>
The National Bank client service method:
<a href="https://github.com/Ee-Cs/CurrencyAccounts/blob/main/src/main/java/kp/services/ExchangeRateService.java#L38">
kp.services.ExchangeRateService::getExchangeRate</a>.<br/>
The National Bank <a href="http://api.nbp.pl/api/exchangerates/rates/a/usd/">exchange rates endpoint</a> 
</p>
<hr>
<h3>❷ The tests</h3>
<p>
The <a href="https://github.com/Ee-Cs/CurrencyAccounts/tree/main/src/test/java/kp">
tests</a>.<br>
The controller tests use JUnit Jupiter and MockMvc.
</p>
<p>
Application tests executed in IntelliJ IDEA<br/>
<img alt="" src="docs/images/ScreenshotTests.png"/>
</p>
<p>
Test scenario executed in Postman 
</p>
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
After these two transactions the account balance is not different from original (with small roundings).  
</p>
<hr>
<h3>❸ The documentation</h3>
<p>
<a href="https://htmlpreview.github.io/?https://github.com/Ee-Cs/CurrencyAccounts/blob/main/docs/apidocs/index.html">
Java API Documentation</a>
</p>
<h3>❹ The future design ideas</h3>
<ul>
<li>authentication and authorization</li>
<li>extend data with dates and more personal data</li>
<li>refactor database schema</li>
<li>switch to PostgreSQL database</li>
<li>save exchange receipts to database</li> 
<li>add endpoint for account deleting</li>
<li>add endpoints for more account transactions types (like money withdrawing etc.)</li>
<li>add endpoints for customer activity reporting</li>
<li>add foreign exchange commission</li>
<li>generate the endpoint from OpenApi yaml file</li>
<li>move to Docker, or Kubernetes, or cloud (AWS, MS Azure or Google Cloud Platform)</li>
</ul>
<hr/>