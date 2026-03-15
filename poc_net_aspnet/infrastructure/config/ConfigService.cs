namespace poc_net_aspnet.infrastructure.config;

public class ConfigService(IConfiguration configuration)
{
    public KeycloakConfig Keycloak { get; } = new(configuration);

    public string AllowedHosts { get; } = configuration["AllowedHosts"]
                                          ?? throw new InvalidOperationException("AllowedHosts is not configured.");
}