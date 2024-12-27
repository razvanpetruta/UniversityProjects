using TMPro;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    // Singleton pattern
    public static GameManager Instance { get; private set; }
    private bool isGamePaused = false;
    
    // if the instance does not exist => create it and do not destroy it
    // if new instances are created => destroy them
    private void Awake()
    {
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            Destroy(gameObject);
        }
    }
    
    // enable or disable the pause menu
    public void PauseGame(Canvas canvas)
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            if (!isGamePaused)
            {
                canvas.enabled = true;
                isGamePaused = true;
            }
            else if (isGamePaused)
            {
                canvas.enabled = false;
                isGamePaused = false;
            }
        }
    }
    
    public void IncrementWins(int player, string playerPrefsName, TextMeshProUGUI textMeshPro, string textValue)
    {
        player++;
        PlayerPrefs.SetInt(playerPrefsName, player);
        textMeshPro.text = textValue;
    }

    public void ResetScore()
    {
        PlayerPrefs.SetInt("player1Wins", 0);
        PlayerPrefs.SetInt("player2Wins", 0);
    }
}