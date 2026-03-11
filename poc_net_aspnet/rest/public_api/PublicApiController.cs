using System.Net.Mime;
using Microsoft.AspNetCore.Mvc;

namespace poc_net_aspnet.rest.public_api;

[ApiController]
[Route(Path)]
public class PublicApiController : ControllerBase
{
    public const string Path = "/public_api";

    [HttpGet("getTest")]
    [Produces(MediaTypeNames.Text.Plain)]
    public ActionResult<string> Hello()
    {
        return StatusCode(StatusCodes.Status200OK, "some test message");
    }
}