package com.appfront.server.user;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appfront.server.AppFrontEnvironment;
import com.appfront.server.repositories.UserRepository;
import com.appfront.server.resources.Tag;
import com.appfront.server.resources.User;

/**
 * User REST endpoints
 * 
 * @author ente
 */
@RestController
@RequestMapping("/user/")
public class RESTUser {
    
    /**
     * A response to a user activation
     * 
     * @author ente
     */
    private final class UserActivationResponse {
        
        private String id;
    }
    @Autowired
    private UserRepository        userRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    
    /**
     * Request a new user.
     * 
     * @return html containing the captcha to solve.
     */
    @RequestMapping(value = "/_new", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
    public String requestNewUser() {
        final ReCaptcha captcha = ReCaptchaFactory.newSecureReCaptcha(AppFrontEnvironment.GOOGLE_API_PUBLIC_KEY, AppFrontEnvironment.GOOGLE_API_PRIVATE_KEY, false);
        return captcha.createRecaptchaHtml(null, null);
    }
    
    /**
     * Register a new user.
     * 
     * @param request
     *            the serverlet request
     * @param recaptchaChallengeField
     *            the recaptcha challenge field
     * @param recaptchaResponseField
     *            the recaptcha response field
     * @return new user id.
     */
    @RequestMapping(value = "/_new", method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public UserActivationResponse registerNewUser(final HttpServletRequest request, final @RequestParam("recaptcha_challenge_field") String recaptchaChallengeField,
            final @RequestParam("recaptcha_response_field") String recaptchaResponseField) {
        final String remoteAddress = request.getRemoteAddr();
        final ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(AppFrontEnvironment.GOOGLE_API_PRIVATE_KEY);
        final ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddress, recaptchaChallengeField, recaptchaResponseField);
        // captcha valid?
        final UserActivationResponse userActivationReturn = new UserActivationResponse();
        // TODO: not always add user
        if (true || reCaptchaResponse.isValid()) {
            // yes: create new user
            User newUser = new User();
            newUser = userRepository.save(newUser);
        } else {
            // no
            userActivationReturn.id = null;
        }
        return userActivationReturn;
    }
    
    /**
     * REST endpoint for position updates.
     * 
     * @param userId
     *            the user's id
     * @param userPosition
     *            the users position
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    public List<User> updatePosition(final @PathVariable Long userId, final @RequestBody GeoPoint userPosition) {
        // update user position
        final UserPosition newPosition = new UserPosition(userPosition, new Date());
        final User updateUser = userRepository.findOne(userId);
        updateUser.updatePosition(newPosition);
        userRepository.save(updateUser);
        // build distance criteria
        Criteria positionCriteria = new Criteria("newestPosition.position").within(updateUser.getLastPosition().getPosition(), AppFrontEnvironment.MAX_REQUEST_DISTANCE);
        Criteria tagCriteria = new Criteria();
        // and tag criteria
        for (final Tag tag : updateUser.getTags()) {
            tagCriteria.or("tags").contains(tag.getTag());
        }
        // build query
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(positionCriteria.and(tagCriteria));
        // get users
        return elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, User.class);
    }
}
