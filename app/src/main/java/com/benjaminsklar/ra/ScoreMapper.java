package com.benjaminsklar.ra;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.UUID;

/**
 * Created by Ben on 5/29/2017.
 */

@DynamoDBTable(tableName = "RaScore")
public class ScoreMapper {
    private String raScoreId;
    private int nPlayers;
    private String [] playerNames;
    private int [] playerScores;

    ScoreMapper() {
        nPlayers = Integer.MIN_VALUE;
        playerNames = new String[Game.nMaxPlayers_c];
        playerScores = new int[Game.nMaxPlayers_c];

        int i;
        for (i = 0; i < playerScores.length; i++) {
            playerScores[i] = Integer.MIN_VALUE;
        }
    }

    @DynamoDBHashKey(attributeName = "RaScoreId")
    public String getRaScoreId() { return raScoreId;}
    public void setRaScoreId(String raScoreId) { this.raScoreId = raScoreId; }

    @DynamoDBAttribute(attributeName = "NPlayers")
    public int getNPlayers() { return nPlayers; }
    public void setNPlayers(int nPlayers) { this.nPlayers = nPlayers; }

    @DynamoDBAttribute(attributeName = "PlayerName1")
    public String getPlayerName1() { return playerNames[0]; }
    public void setPlayerName1(String playerName) { this.playerNames[0] = playerName; }

    @DynamoDBAttribute(attributeName = "PlayerScore1")
    public int getPlayerScore1() { return playerScores[0]; }
    public void setPlayerScore1(int playerScore) { this.playerScores[0] = playerScore; }

    @DynamoDBAttribute(attributeName = "PlayerName2")
    public String getPlayerName2() { return playerNames[1]; }
    public void setPlayerName2(String playerName) { this.playerNames[1] = playerName; }

    @DynamoDBAttribute(attributeName = "PlayerScore2")
    public int getPlayerScore2() { return playerScores[1]; }
    public void setPlayerScore2(int playerScore) { this.playerScores[1] = playerScore; }

    @DynamoDBAttribute(attributeName = "PlayerName3")
    public String getPlayerName3() { return playerNames[2]; }
    public void setPlayerName3(String playerName) { this.playerNames[2] = playerName; }

    @DynamoDBAttribute(attributeName = "PlayerScore3")
    public int getPlayerScore3() { return playerScores[2]; }
    public void setPlayerScore3(int playerScore) { this.playerScores[2] = playerScore; }

    @DynamoDBAttribute(attributeName = "PlayerName4")
    public String getPlayerName4() { return playerNames[3]; }
    public void setPlayerName4(String playerName) { this.playerNames[3] = playerName; }

    @DynamoDBAttribute(attributeName = "PlayerScore4")
    public int getPlayerScore4() { return playerScores[3]; }
    public void setPlayerScore4(int playerScore) { this.playerScores[3] = playerScore; }

    @DynamoDBAttribute(attributeName = "PlayerName5")
    public String getPlayerName5() { return playerNames[4]; }
    public void setPlayerName5(String playerName) { this.playerNames[4] = playerName; }

    @DynamoDBAttribute(attributeName = "PlayerScore5")
    public int getPlayerScore5() { return playerScores[4]; }
    public void setPlayerScore5(int playerScore) { this.playerScores[4] = playerScore; }

    public void setValues() {
        Game game = Game.getInstance();
        raScoreId = UUID.randomUUID().toString(); // TODO: replace with DynamoDB- attribute
        nPlayers = game.getNPlayers();
        int i;
        Player [] players = game.getPlayers();
        for (i = 0; i < nPlayers; i++) {
            playerNames[i] = players[i].getName();
            playerScores[i] = players[i].aiScoreEpoch[Player.iScoreTotal_c];
        }
    }

    void save() {
        final ScoreMapper scoreMapper = this;
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    AmazonDynamoDBClient dbClient = RaApplication.getRaApplication().getDbClient();
                    DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
                    mapper.save(scoreMapper);
                }
                catch (Exception e) {
                    Log.d(ScoreMapper.class.toString(), e.toString());
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }
}
