using UnityEngine;
using UnityEngine.SceneManagement;

public class Menu : MonoBehaviour
{
    private GameManager gameManager;
    [SerializeField] private Canvas pauseCanvas = null;

    private void Start()
    {
        gameManager = FindObjectOfType<GameManager>().GetComponent<GameManager>();
    }

    private void Update()
    {
        PauseGame();
    }

    public void Play()
    {
        gameManager.ResetScore();
        Invoke("LateLoad", 0.5f);
    }

    private void LateLoad()
    {
        SceneManager.LoadScene(1);
    }

    public void Settings()
    {
        Debug.Log("Settings");
    }

    public void MainMenu()
    {
        SceneManager.LoadScene(0);
    }

    public void Quit()
    {
        Application.Quit();
    }

    public void PauseGame()
    {
        if (SceneManager.GetActiveScene().buildIndex != 1) 
            return;
        
        gameManager.PauseGame(pauseCanvas); ;
    }
}
