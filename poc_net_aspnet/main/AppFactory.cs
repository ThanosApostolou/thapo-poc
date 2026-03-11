namespace poc_net_aspnet.main;

public static class AppFactory
{
    public static WebApplication Create(string[] args)
    {
        var builder = CreateBuilder(args);

        var app = builder.Build();
        // Configure the HTTP request pipeline.
        app.MapOpenApi();
        app.UseSwaggerUI(options => { options.SwaggerEndpoint("/openapi/v1.json", "v1"); });
        app.UsePathBase("/pocaspnet");
        app.UseRouting();
        app.MapControllers();


        return app;
    }

    private static WebApplicationBuilder CreateBuilder(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);
        // Add services to the container.
        // Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
        builder.Services.AddControllers();
        builder.Services.AddOpenApi();
        return builder;
    }
}