package com.appfront.server.user;

<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

<<<<<<< HEAD
=======
import org.apache.log4j.Logger;
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.MediaType;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.stereotype.Controller;
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.ResponseBody;
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
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
<<<<<<< HEAD
=======
@Controller
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
@RestController
@RequestMapping("/user/")
public class RESTUser {
    
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    
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
<<<<<<< HEAD
     * @return new user id.
     */
    @RequestMapping(value = "/_new", method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public UserActivationResponse registerNewUser(final HttpServletRequest request, final @RequestParam("recaptcha_challenge_field") String recaptchaChallengeField,
            final @RequestParam("recaptcha_response_field") String recaptchaResponseField) {
=======
     * @param userPosition
     *            geo point of the user to add
     * @return new user id.
     */
    @RequestMapping(value = "/_new", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    User registerNewUser(final HttpServletRequest request, final @RequestParam("recaptcha_challenge_field") String recaptchaChallengeField,
            final @RequestParam("recaptcha_response_field") String recaptchaResponseField, final @RequestBody GeoPoint userPosition) {
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
        final String remoteAddress = request.getRemoteAddr();
        final ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(AppFrontEnvironment.GOOGLE_API_PRIVATE_KEY);
        final ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddress, recaptchaChallengeField, recaptchaResponseField);
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
            tagCriteria.or("tags").contains(tag.getTag());
        }
        // build query
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(positionCriteria.and(tagCriteria));
        // get users
        return elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, User.class);
    }
}
