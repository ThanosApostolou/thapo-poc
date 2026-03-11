using System.Net.Mime;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace poc_net_aspnet.rest.public_api;

[ApiController]
[Route(ControllerPath)]
public class PublicApiController : ControllerBase
{
    public const string ControllerPath = "/public_api";

    [HttpGet("getTest")]
    [Produces(MediaTypeNames.Text.Plain)]
    public Ok<string> Hello()
    {
        return TypedResults.Ok("some test message");
    }
}