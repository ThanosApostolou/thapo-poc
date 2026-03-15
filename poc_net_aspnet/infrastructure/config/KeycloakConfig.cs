namespace poc_net_aspnet.infrastructure.config;

public class KeycloakConfig(IConfiguration configuration)
{
    public string Authority { get; } = configuration["Keycloak:Authority"]
                                       ?? throw new InvalidOperationException(
                                           "Keycloak:Authority is not configured.");

    public string Audience { get; } = configuration["Keycloak:Audience"]
                                      ?? throw new InvalidOperationException(
                                          "Keycloak:Audience is not configured.");
}