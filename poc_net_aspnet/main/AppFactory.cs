using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.OpenApi;
using poc_net_aspnet.infrastructure.auth;
using poc_net_aspnet.infrastructure.config;

namespace poc_net_aspnet.main;

public static class AppFactory
{
    public static WebApplication Create(string[] args)
    {
        var builder = CreateBuilder(args);

        var app = builder.Build();
        // Configure the HTTP request pipeline.
        // app.MapOpenApi();
        app.UsePathBase("/pocaspnet");
        app.UseSwagger();
        app.UseSwaggerUI();
        app.UseRouting();
        app.UseAuthentication();
        app.UseAuthorization();
        app.MapControllers();

        return app;
    }

    private static WebApplicationBuilder CreateBuilder(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);


        // Add services to the container.
        builder.Services.AddSingleton<ConfigService>();
        builder.Services.AddControllers();
        builder.Services.AddEndpointsApiExplorer();

        BuilderAddAuthentication(builder);
        BuilderAddAuthorization(builder);


        builder.Services.AddSwaggerGen(options =>
        {
            var securityScheme = new OpenApiSecurityScheme
            {
                Name = "Authorization",
                Type = SecuritySchemeType.Http,
                Scheme = "bearer",
                BearerFormat = "JWT",
                In = ParameterLocation.Header,
                Description = "Enter your Keycloak JWT Bearer token."
            };
            options.AddSecurityDefinition(JwtBearerDefaults.AuthenticationScheme, securityScheme);
            options.AddSecurityRequirement(_ => new OpenApiSecurityRequirement
            {
                {
                    new OpenApiSecuritySchemeReference(JwtBearerDefaults.AuthenticationScheme),
                    []
                }
            });
        });

        return builder;
    }

    private static void BuilderAddAuthentication(WebApplicationBuilder builder)
    {
        builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer();
        builder.Services.ConfigureOptions<JwtBearerOptionsConfigurer>();
    }

    private static void BuilderAddAuthorization(WebApplicationBuilder builder)
    {
        builder.Services.AddAuthorization();
    }
}