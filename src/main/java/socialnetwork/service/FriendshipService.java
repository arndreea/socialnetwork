package socialnetwork.service;

import socialnetwork.model.*;
import socialnetwork.model.validators.*;
import socialnetwork.repository.db.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FriendshipService {
    private final UserDBRepository userDBRepository;
    private final FriendshipDBRepository friendshipDBRepository;

    public FriendshipService(UserDBRepository userDBRepository, FriendshipDBRepository friendshipDBRepository) {
        this.userDBRepository = userDBRepository;
        this.friendshipDBRepository = friendshipDBRepository;
    }

    /**
     *adds a friendship between 2 existing users
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     */
    public void addFriendship(String username1, String username2) {
        validateFriendship(username1, username2);

        Friendship friendship = new Friendship(username1, username2, LocalDateTime.now());

        Validator<Friendship> validator = new FriendshipValidator();
        validator.validate(friendship);

        Tuple<String> newFriendship = new Tuple<>(username1, username2);
        newFriendship.orderTuple();
        if(friendshipDBRepository.findOne(newFriendship) != null)
            throw new ValidationException("This friendship already exists!\n");

        friendshipDBRepository.save(friendship);
    }

    /**
     *deletes a friendship between 2 existing users
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     */
    public void removeFriendship(String username1, String username2) {
        validateFriendship(username1, username2);

        Tuple<String> newFriendship = new Tuple<>(username1, username2);
        newFriendship.orderTuple();
        if(friendshipDBRepository.findOne(newFriendship) == null)
            throw new ValidationException("This friendship doesn't exist!\n");

        friendshipDBRepository.delete(newFriendship);
    }

    /**
     *displays the friends of a certain user and the date their friendship was created
     * @param username the username of the user that we requested the friend list for
     */
    public void friendList(String username){
        if(userDBRepository.findOne(username) == null)
            throw new ValidationException("A user with this username doesn't exist!\n");

        List<UserDTO> result = getFriendList(username);

        if(result.size() == 0)
            System.out.println("This user doesn't have any friends! :(");
        else {
            System.out.println("The friend list for this user is : ");
            result.forEach(System.out::println);
        }
    }

    /**
     *displays the friends of a certain user from a certain month and the date their friendship began
     * @param username the username of the user that we requested the friend list for
     * @param month the month the friendship was created
     */
    public void friendListByMonth(String username, String month){
        if(userDBRepository.findOne(username) == null)
            throw new ValidationException("A user with this username doesn't exist!\n");

        int m;
        try {
            m = Integer.parseInt(month);
        } catch(NumberFormatException e) {
            throw new ValidationException("This is not an integer month value!");
        }
        if(m < 1 || m > 12)
            throw new ValidationException("This is not a valid month value!");

        List<UserDTO> result = getFriendList(username).stream()
                .filter(e -> e.getDateOfFriendship().getMonthValue() == m)
                .collect(Collectors.toList());

        if(result.size() == 0)
            System.out.println("This user didn't make any friends in this month! :(");
        else {
            System.out.println("The friend list for this user from the solicited month is : ");
            result.forEach(System.out::println);
        }
    }

    /**
     *gets the friendship list for a certain user
     * @param username the username of the user that we requested the friend list for
     * @return list of friends
     */
    private List<UserDTO> getFriendList(String username){
        List<UserDTO> result1 = StreamSupport.stream(getAll().spliterator(), false)
                .filter(e -> Objects.equals(e.getId().getFirst(), username))
                .map(e -> new UserDTO(userDBRepository.findOne(e.getId().getSecond()).getFirstName(), userDBRepository.findOne(e.getId().getSecond()).getLastName(), e.getDate()))
                .collect(Collectors.toList());

        List<UserDTO> result2 = StreamSupport.stream(getAll().spliterator(), false)
                .filter(e -> Objects.equals(e.getId().getSecond(), username))
                .map(e -> new UserDTO(userDBRepository.findOne(e.getId().getFirst()).getFirstName(), userDBRepository.findOne(e.getId().getFirst()).getLastName(), e.getDate()))
                .collect(Collectors.toList());

        return Stream.concat(result1.stream(), result2.stream())
                .collect(Collectors.toList());
    }

    /**
     *standard validator for 2 usernames
     *we only care if we find the usernames in our repository, not if they're valid or if both the usernames are different
     *if they are not valid, they will not be found in our repository; if the usernames are identical then a friendship can't be created
     * @param username1 first username
     * @param username2 second username
     */
    private void validateFriendship(String username1, String username2) {
        if(username1 == null && username2 == null)
            throw new ValidationException("Invalid usernames!\n");
        if(username1 == null)
            throw new ValidationException("Invalid first username!\n");
        if(username2 == null)
            throw new ValidationException("Invalid second username!\n");

        if(userDBRepository.findOne(username1) == null && userDBRepository.findOne(username2) == null)
            throw new ValidationException("Both users are non-existent!\n");
        if(userDBRepository.findOne(username1) == null)
            throw new ValidationException("First user is non-existent!\n");
        if(userDBRepository.findOne(username2) == null)
            throw new ValidationException("Second user is non-existent!\n");
    }

    public Iterable<Friendship>getAll() {
        return friendshipDBRepository.findAll();
    }
}