package com.appfront.server.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@Controller
@RestController
@RequestMapping("/user/")
public class RESTUser {
    
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    /**
     * The user repository
     */
    @Autowired
    private UserRepository        userRepository;
    /**
     * The logger.
     */
    private final Logger          logger = Logger.getLogger(this.getClass());
    
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
     * @param userPosition
     *            geo point of the user to add
     * @return new user id.
     */
    @RequestMapping(value = "/_new", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    User registerNewUser(final HttpServletRequest request, final @RequestParam("recaptcha_challenge_field") String recaptchaChallengeField,
            final @RequestParam("recaptcha_response_field") String recaptchaResponseField, final @RequestBody GeoPoint userPosition) {
        final String remoteAddress = request.getRemoteAddr();
        final ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(AppFrontEnvironment.GOOGLE_API_PRIVATE_KEY);
        final ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddress, recaptchaChallengeField, recaptchaResponseField);
        User newUser;
        // captcha valid?
        // TODO: not always add user
        if (true || reCaptchaResponse.isValid()) {
            // yes: create new user
            newUser = new User(userRepository, userPosition);
            logger.info("New user: " + newUser);
        }
        return newUser;
    }
    
    /**
     * REST endpoint for user updates.
     * 
     * @param updateUser
     *            the user to update
     * @return a list of users close to the user with same tags
     */
    @RequestMapping(value = "_update", method = RequestMethod.POST)
    public List<User> updatePosition(final @RequestBody User updateUser) {
        final User user = new User(userRepository, updateUser.getId(), updateUser.getSecret());
        // update user information
        user.setNewestPosition(updateUser.getNewestPosition().getPosition());
        user.setTags(updateUser.getTags());
        // get other users
        // build distance criteria
        Criteria positionCriteria = new Criteria("newestPosition.position").within(user.getNewestPosition().getPosition(), AppFrontEnvironment.MAX_REQUEST_DISTANCE);
        Criteria tagCriteria = new Criteria();
        // and tag criteria
        for (final Tag tag : user.getTags()) {
            tagCriteria.or("tags").contains(tag.getTag());
        }
        // build query
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(positionCriteria.and(tagCriteria));
        // get users
        return elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, User.class);
    }
}
