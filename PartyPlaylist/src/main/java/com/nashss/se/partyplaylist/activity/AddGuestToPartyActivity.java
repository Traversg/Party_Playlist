package com.nashss.se.partyplaylist.activity;

import java.util.ArrayList;
import java.util.List;

import com.nashss.se.partyplaylist.activity.requests.AddGuestToPartyRequest;
import com.nashss.se.partyplaylist.activity.results.AddGuestToPartyResult;

import com.nashss.se.partyplaylist.converters.ModelConverter;
import com.nashss.se.partyplaylist.dynamodb.UserDAO;
import com.nashss.se.partyplaylist.dynamodb.models.User;
import com.nashss.se.partyplaylist.models.UserModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;



/**
 * Implementation of the AddGuestToPartyActivity for the PartyPlaylist AddGuestToParty API.
 *
 * This API allows the host to add a guest to their existing party.
 */
public class AddGuestToPartyActivity {

    private final Logger log = LogManager.getLogger();
    private final UserDAO userDAO;

    /**
     * AddGuestToPartyAcitivty Constructor
     *
     * @param userDAO to initialize the userDAO;
     */

    @Inject
    public AddGuestToPartyActivity(UserDAO userDao) {
        this.userDAO = userDao;
    }


    /**
     * This method handles the incoming request by adding a guest
     * to the party and persisting the updated guestList.
     * <p>
     * It then returns the updated guestList.
     * <p>
     * If the user does not exist, this should throw a UserNotFoundException.
     *
     * @param addGuestToPartyRequest request object containing the user ID
     *                                 to retrieve the user data
     * @return addGuestToPartyResult result object containing the party's updated list of
     *                                 API defined {@link UserModel}s
     */


    public AddGuestToPartyResult handleRequest(final AddGuestToPartyRequest addGuestToPartyRequest) {
        log.info("Received AddGuestToPartyRequest {} ", addGuestToPartyRequest);

        String userId = addGuestToPartyRequest.getUserId();
        User guestToAdd = userDAO.getGuest(userId);

        List<User> guestList = userDAO.getGuestList();
        guestList.add(guestToAdd);

        ModelConverter convert = new ModelConverter();
        List<UserModel> userModels = new ArrayList<>();

        for (User user : guestList) {
            userModels.add(convert.toUserModel(user));
        }

        return AddGuestToPartyResult.builder()
                .withGuestList(userModels)
                .build();

    }
}
