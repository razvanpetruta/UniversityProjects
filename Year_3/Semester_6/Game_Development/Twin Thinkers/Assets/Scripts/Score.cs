using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Playables;
using UnityEngine.SceneManagement;

public class Score : MonoBehaviour
{
    [SerializeField] private TextMeshProUGUI scoreText;
    [SerializeField] private TextMeshProUGUI winText;
    [SerializeField] private PlayableDirector roundEnd;
    
    private int player1Wins = 0;
    private int player2Wins = 0;

    private int player1Victories = 0;
    private int player2Victories = 0;

    private GameManager gameManager;
    public void Start()
    {
        gameManager = FindObjectOfType<GameManager>().GetComponent<GameManager>();
        
        player1Wins = PlayerPrefs.GetInt("player1Wins");
        player2Wins = PlayerPrefs.GetInt("player2Wins");

        player1Victories = PlayerPrefs.GetInt("player1Victories");
        player2Victories = PlayerPrefs.GetInt("player2Victories");
        
        scoreText.text = player1Wins + " - " + player2Wins;
    }
    
    void Update()
    {
        scoreText.text = player1Wins + " - " + player2Wins;
    }

    public void Player1Victory()
    {
        string textValue = "P1 WINS";
        
        if (player1Wins == 4)
        {
            Invoke("GameOver", 2.5f);
            textValue = "GAME OVER";
            player1Victories++;
            PlayerPrefs.SetInt("player1Victories", player1Victories);
        }
        
        gameManager.IncrementWins(player1Wins, "player1Wins", winText, textValue);
    }
    
    public void Player2Victory()
    {
        string textValue = "P2 WINS";
        
        if (player2Wins == 4)
        {
            Invoke("GameOver", 2.5f); 
            textValue = "GAME OVER";
            player2Victories++;
            PlayerPrefs.SetInt("player2Victories", player2Victories);
        }
        
        gameManager.IncrementWins(player2Wins, "player2Wins", winText, textValue);
    }
    
    public void RoundEndAnimation()
    {
        roundEnd.Play();
    }

    public void StartRound()
    {
        Invoke("LoadScene", 2.5f);
    }

    private void LoadScene()
    {
        SceneManager.LoadScene(1);
    }
    
    private void GameOver()
    {
        SceneManager.LoadScene(2);
    }
}
