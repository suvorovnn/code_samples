package test.com.suv.cgpt;

import com.suv.cgpt.CgptClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.Properties;


public class CgptTest {
    public static Properties prop = new Properties();
    public static final String RUN_TESTS = "run.tests";

    @BeforeAll
    static void onStart() {

        InputStream inputStream = CgptTest.class.getResourceAsStream("/test.properties");

        if (inputStream == null) {
            System.out.println("Cannot find /test.properties");
            System.out.println("Place test.properties which contain cpgt.api.key property");
            return;
        }

        try (inputStream) {

            prop.load(inputStream);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        System.setProperty(RUN_TESTS, "true");

    }

    @Test
    public void runTests() {
        if (Boolean.getBoolean(RUN_TESTS)) {
            rollDice();
        }
    }

    public void rollDice() {
        CgptClient client = new CgptClient();
        client.setProperties(prop::getProperty);
        CgptMessage msg = new CgptMessage();
        msg.role("Dice roller generator.");
        msg.content("Roll d20 dice");
        msg.temperature(0.7f);
        System.out.println(msg.getText());
        String s = client.doRequest(msg.getText());
        System.out.println(s);
    }
}