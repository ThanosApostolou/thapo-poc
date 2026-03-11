using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace poc_net_aspnet.rest.public_api.file;

[ApiController]
[Route(ControllerPath)]
public class FileController(ILogger<FileController> logger) : ControllerBase
{
    public const string ControllerPath = PublicApiController.ControllerPath + "/file";

    [HttpGet("fetch_image")]
    public PhysicalFileHttpResult FetchImage()
    {
        logger.LogInformation("START fetchImage");
        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-image.jpg");
        logger.LogInformation("END fetchImage");

        return TypedResults.PhysicalFile(filePath, "image/jpeg", enableRangeProcessing: true);
    }

    [HttpGet("fetch_audio")]
    public PhysicalFileHttpResult FetchAudio()
    {
        logger.LogInformation("START fetchAudio");

        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-audio.mp3");
        logger.LogInformation("END fetchAudio");

        return TypedResults.PhysicalFile(filePath, "audio/mpeg", enableRangeProcessing: true);
    }

    [HttpGet("fetch_video")]
    public PhysicalFileHttpResult FetchVideo()
    {
        logger.LogInformation("START fetchVideo");
        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-video.webm");
        logger.LogInformation("END fetchVideo");
        return TypedResults.PhysicalFile(filePath, "video/webm", enableRangeProcessing: true);
    }
}