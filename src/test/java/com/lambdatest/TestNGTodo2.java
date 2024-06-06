package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import okhttp3.*;

public class TestNGTodo2 {

    private RemoteWebDriver driver;
    private String status = "failed";

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = "ritamg"; // Replace with your LambdaTest username
        String authkey = "accss_key"; // Replace with your LambdaTest access key

        String hub = "@hub.lambdatest.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("124");

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("project", "Untitled");
        ltOptions.put("network.http2", true);
        ltOptions.put("video", true);
        ltOptions.put("w3c", true);

        browserOptions.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), browserOptions);
    }

    @Test
    public void basicTest() throws InterruptedException {
        try {
            driver.get("https://stage.usta.com/en/home.html");
            Thread.sleep(12000);
            System.out.println("Loading the URL");
            Thread.sleep(75000);

            OkHttpClient client = new OkHttpClient.Builder().build();

            Request request = new Request.Builder()
                    .url("https://api.lambdatest.com/automation/api/v1/sessions/" + driver.getSessionId() + "?shareExpiryLimit=10")
                    .method("GET", null)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", Credentials.basic("ritamg", "accss_key"))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONObject data = jsonResponse.getJSONObject("data");
                int buildId = data.getInt("build_id");

                System.out.println("Build ID: " + buildId);
                System.out.println("https://automation.lambdatest.com/build?&build=" + buildId + "&statusTab=All");

                // Fetch build information
                fetchBuildInformation(buildId);

                status = "passed";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "failed";
        }
    }

    private void fetchBuildInformation(int buildId) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.lambdatest.com/automation/api/v1/builds/" + buildId + "?shareExpiryLimit=30")
                .method("GET", null)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", Credentials.basic("ritamg", "accss_key"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONObject data = jsonResponse.getJSONObject("data");
                String public_url = String.valueOf(data.getString("public_url"));
                System.out.println("Build Information: " + responseBody);
                System.out.println("The public url is: "+public_url);

                // Further parsing of build information if needed
            } else {
                System.out.println("Failed to retrieve build information. Response code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            driver.executeScript("lambda-status=" + status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
