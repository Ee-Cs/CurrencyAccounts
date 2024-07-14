package kp;

import kp.controller.KpController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The SpringBoot application tests.
 */
@SpringBootTest
class ApplicationTests {

    private final KpController kpController;

    /**
     * The constructor.
     *
     * @param kpController the controller
     */
    ApplicationTests(@Autowired KpController kpController) {
        this.kpController = kpController;
    }

    /**
     * Should load the context for the {@link KpController}.
     * <p>
     * It is the sanity check test that will fail, if the application context cannot start.
     * </p>
     */
    @Test
    @DisplayName("🟩 should load context")
    void shouldLoadContext() {
        assertThat(kpController).isNotNull();
    }

}
