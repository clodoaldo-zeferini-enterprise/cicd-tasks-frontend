package br.ce.wcaquino.tasksfrontend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "appsettings")
public class AppSettings {
    public String getEnvironment() {
        return Environment;
    }

    public void setEnvironment(String environment) {
        Environment = environment;
    }

    private String Environment;

    private Application application = new Application();
    private Backend backend = new Backend();
    private Server server = new Server();

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

}
