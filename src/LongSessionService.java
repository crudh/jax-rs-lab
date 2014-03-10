import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Stateless
@Path("/lss")
public class LongSessionService {
    private final static int COOKIE_AGE_SECONDS = 60 * 60 * 24 * 7;
    private final static String HASH_KEY = "kallekula";
    private final static String HASH_KEY_ALGORITHM = "HmacSHA1";
    private final static String HASH_ENCODING = "UTF-8";
    // FIXME CR: Set this to true after testing (but requires HTTPS)
    private final static boolean SECURE_COOKIE = false;

    @GET
    @Path("/login/{userName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response doLogin(@Context HttpServletRequest req, @PathParam("userName") String userName)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Date expiryDate = new Date();
        expiryDate.setTime(expiryDate.getTime() + (COOKIE_AGE_SECONDS * 1000));
        String expiryDateString = String.valueOf(expiryDate.getTime());

        NewCookie userCookie = createCookie("userName", userName);
        NewCookie expiryCookie = createCookie("expiry", expiryDateString);
        NewCookie hashCookie = createCookie("hash", hashValue(userName + expiryDateString));

        HttpSession session = req.getSession(true);
        session.setAttribute("userName", userName);
        session.setAttribute("loggedIn", Boolean.TRUE);

        String response = "Logged in " + userName;

        return Response.ok(response, MediaType.TEXT_PLAIN).cookie(userCookie, expiryCookie,
                hashCookie).build();
    }

    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkStatus(@Context HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        String userName = (String)session.getAttribute("userName");
        Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");

        return userName + " is logged in: " + loggedIn;
    }

    @GET
    @Path("/validate")
    @Produces(MediaType.TEXT_PLAIN)
    public String doValidate(@CookieParam("userName") String userName,
                             @CookieParam("expiry") String expiry,
                             @CookieParam("hash") String hash) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException {

        return "Validation ok: " + validateValue(userName + expiry, hash);
    }

    private NewCookie createCookie(String name, String value) {
        Date expiryDate = new Date();
        expiryDate.setTime(COOKIE_AGE_SECONDS * 1000);

        Cookie cookie = new Cookie(name, value, "/", "");

        return new NewCookie(cookie, "", COOKIE_AGE_SECONDS, expiryDate, SECURE_COOKIE, true);
    }

    private String hashValue(String value) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec key = new SecretKeySpec(HASH_KEY.getBytes("UTF-8"), HASH_KEY_ALGORITHM);

        Mac mac = Mac.getInstance(HASH_KEY_ALGORITHM);
        mac.init(key);

        byte[] hash = mac.doFinal(value.getBytes(HASH_ENCODING));

        return new String(Base64.encodeBase64(hash), HASH_ENCODING);
    }

    private boolean validateValue(String value, String hash) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException {
        String comparisonHash = hashValue(value);

        return comparisonHash.equals(hash);
    }

}
