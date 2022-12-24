package io.github.indroDevTeam.indroMain.data;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.model.Rank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataAPI {
    void deinit();


    /* Profile */
    // create
    boolean createProfile(Profile profile);

    // read
    Optional<Profile> getProfile(UUID userId);

    // update
    boolean updateProfile(UUID userId, Profile newProfile);

    // delete
    boolean deleteProfile(UUID userId);

    /* Points */
    // create
    boolean createPoint(Point point);

    // read
    Optional<Point> getPoint(UUID ownerId, String name);
    List<Point> getPointByOwner(UUID ownerId);
    List<Point> getAllPoints();

    // update
    boolean updatePoint(UUID ownerId, String name, Point newPoint);

    // delete
    boolean deletePoint(UUID ownerId, String name);
    
    /* Rank */
    // create
    boolean createRank(Rank rank);
    
    // read
    Optional<Rank> getRank(String name);

    List<Rank> getAllRanks();
    
    // update
    boolean updateRank(String name, Rank newRank);

    // delete
    boolean deleteRank(String name);
}
