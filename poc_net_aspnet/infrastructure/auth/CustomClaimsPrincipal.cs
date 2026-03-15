using System.Security.Claims;

namespace poc_net_aspnet.infrastructure.auth;

public class CustomClaimsPrincipal : ClaimsPrincipal
{
    public CustomClaimsPrincipal(ClaimsPrincipal principal) : base(principal)
    {
    }

    public string? Subject { get; } = "";

    public string? Username => FindFirst("preferred_username")?.Value;

    public string? Email => FindFirst(ClaimTypes.Email)?.Value
                            ?? FindFirst("email")?.Value;


    public IEnumerable<string> Roles =>
        FindAll(ClaimTypes.Role).Select(c => c.Value)
            .Concat(FindAll("realm_access/roles").Select(c => c.Value));
}