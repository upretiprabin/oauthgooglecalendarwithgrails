package oauthgooglecalendar

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.Json
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.CalendarScopes
import com.prabin.QuickStart
import grails.converters.JSON
import org.grails.web.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService
import org.scribe.model.Verb;



import javax.servlet.http.HttpSession;


class CalendarController {

    private static final String CLIENT_ID = "303690030146-unbtbldorkmviusct2ikriun5a65iotq.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "Hb3xg25ol5EDHxR2oe6PjKi3";
    String APPLICATION_NAME = "OAuthSampleProject";

// Set up the HTTP transport and JSON factory
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    def index = {

    }

    def login = {


        //Configure
        ServiceBuilder builder= new ServiceBuilder();
        OAuthService service = builder.provider(QuickStart.class)
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .callback("http://localhost:8080/calendar/oauth2callback")
                .scope(CalendarScopes.CALENDAR_READONLY)
                .build(); //Now build the call

        session.setAttribute("oauth2Service", service);
        response.sendRedirect(service.getAuthorizationUrl(null));

    }

    def getEvents = {

        println "params = $params"
        render "$params"

    }

    def oauth2callback = {

        println " oauth2callback from api called "

        OAuthService service = session.oauth2Service
        def items
        if(!session.token){
            def authCode = request.getParameter("code")
            //Construct the access token
            Token token = service.getAccessToken(null, new Verifier(authCode))
            session.setAttribute("token",token.toString())
            OAuthRequest oReQ = new OAuthRequest(Verb.GET,"https://www.googleapis.com/calendar/v3/calendars/upretiprabin7946@gmail.com/events")
            service.signRequest(token,oReQ)
            Response oResp = oReQ.send()
            JSONObject jsonObj = new JSONObject(new String(oResp.getBody().getBytes()))
            items = jsonObj.items
            session.items = items
        }
        session.items.each{
            println "it = $it"
            println "it = ${it.getClass()}"
        }
        render (template:"getEvents")

        return
    }



}
