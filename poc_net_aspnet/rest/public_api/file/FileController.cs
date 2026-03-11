using System.Net.Mime;
using Microsoft.AspNetCore.Mvc;

namespace poc_net_aspnet.rest.public_api.file;

[ApiController]
[Route(ControllerPath)]
public class FileController(ILogger<FileController> logger) : ControllerBase
{
    public const string ControllerPath = PublicApiController.ControllerPath + "/file";

    [HttpGet("fetch_image")]
    [Produces(MediaTypeNames.Image.Jpeg)]
    public PhysicalFileResult FetchImage()
    {
        logger.LogInformation("START fetchImage");
        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-image.jpg");
        logger.LogInformation("END fetchImage");

        return PhysicalFile(filePath, "image/jpeg", true);
    }

    [HttpGet("fetch_audio")]
    [Produces("audio/mpeg")]
    public PhysicalFileResult FetchAudio()
    {
        logger.LogInformation("START fetchAudio");

        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-audio.mp3");
        logger.LogInformation("END fetchAudio");

        return PhysicalFile(filePath, "audio/mpeg", true);
    }

    [HttpGet("fetch_video")]
    [Produces("video/webm")]
    public PhysicalFileResult FetchVideo()
    {
        logger.LogInformation("START fetchVideo");

        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "../files/public/sample-video.webm");

        logger.LogInformation("END fetchVideo");

        return PhysicalFile(filePath, "video/webm", true);
    }
}