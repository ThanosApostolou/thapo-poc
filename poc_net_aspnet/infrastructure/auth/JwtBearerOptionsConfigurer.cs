using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.Options;
using poc_net_aspnet.infrastructure.config;

namespace poc_net_aspnet.infrastructure.auth;

/// <summary>
///     Configures JwtBearerOptions.Events using proper DI so that ILogger is
///     created once (singleton) instead of on every token validation call.
/// </summary>
public class JwtBearerOptionsConfigurer(
    ILogger<JwtBearerOptionsConfigurer> logger,
    ConfigService config)
    : IConfigureNamedOptions<JwtBearerOptions>
{
    public void Configure(string? name, JwtBearerOptions options)
    {
        options.Authority = config.Keycloak.Authority;
        options.RequireHttpsMetadata = false; // allow HTTP for local dev
        options.Audience = config.Keycloak.Audience;
        options.Events = new JwtBearerEvents
        {
            OnTokenValidated = JwtBearerEventOnTokenValidated
        };
    }

    public void Configure(JwtBearerOptions options)
    {
        Configure(Options.DefaultName, options);
    }

    private Task JwtBearerEventOnTokenValidated(TokenValidatedContext ctx)
    {
        var customPrincipal = new CustomClaimsPrincipal(ctx.Principal!);
        ctx.Principal = customPrincipal;
        logger.LogInformation(
            "Token validated for subject: {Subject}, claims: [{Claims}]",
            customPrincipal.Subject,
            string.Join(", ", customPrincipal.Claims.Select(c => $"{c.Type}={c.Value}")));
        return Task.CompletedTask;
    }
}