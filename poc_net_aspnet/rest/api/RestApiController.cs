using System.Net.Mime;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using poc_net_aspnet.infrastructure.auth;

namespace poc_net_aspnet.rest.api;

[ApiController]
[Authorize]
[Route(ControllerPath)]
public class RestApiController(ILogger<RestApiController> logger) : ControllerBase
{
    public const string ControllerPath = "/api";

    [HttpGet("getTest")]
    [Produces(MediaTypeNames.Text.Plain)]
    public Ok<string> Hello()
    {
        var principal = HttpContext.User as CustomClaimsPrincipal;
        logger.LogInformation("principal.Claims={0}", principal?.Claims);
        var claims = principal?.Claims
            .Select(c => new { c.Type, c.Value })
            .ToList();
        return TypedResults.Ok("some test message from protected api");
    }
}