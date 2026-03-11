using System.Net.Mime;
using System.Runtime.CompilerServices;
using Microsoft.AspNetCore.Mvc;

namespace poc_net_aspnet.rest.public_api.stream;

[ApiController]
[Route(ControllerPath)]
public class StreamController : ControllerBase
{
    public const string ControllerPath = PublicApiController.ControllerPath + "/stream";

    [HttpGet("fetch_stream")]
    [Produces(MediaTypeNames.Application.Json)]
    public async IAsyncEnumerable<string> FetchStream(CancellationToken cancellationToken)
    {
        for (var i = 1; i <= 3; i++)
        {
            yield return $"Chunk {i}\n";
            await Task.Delay(1000, cancellationToken); // Simulate delay
        }
    }


    private async IAsyncEnumerable<string> GenerateEvents([EnumeratorCancellation] CancellationToken ct)
    {
        for (var i = 1; i <= 3; i++)
        {
            yield return $"Chunk {i}";
            await Task.Delay(1000, ct); // Αναμονή 2 δευτερολέπτων
        }
    }

    [HttpGet("fetch_sse")]
    public IResult FetchSse(CancellationToken cancellationToken)
    {
        var events = GenerateEvents(cancellationToken);

        return Results.ServerSentEvents(events);
    }
}