using System.Collections;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class UIText : MonoBehaviour
{
    public float speed = 0.05f;
    [SerializeField] private TextMeshProUGUI textMeshPro;
    private string fullText;
    
    [SerializeField] private string[] tutorialText = new string[5];
    private int index = 0;
    
    [SerializeField] private Button _button;

    void Start()
    {
        textMeshPro.text = "";
        fullText = tutorialText[index++];

        StartCoroutine(ShowText());
    }

    public void ClickNext()
    {
        if (index == tutorialText.Length)
        {
            this.gameObject.SetActive(false);
            return;
        }
        textMeshPro.text = "";
        fullText = tutorialText[index++];
        StartCoroutine(ShowText());
    }

    IEnumerator ShowText()
    {
        _button.interactable = false;
        for (int i = 0; i < fullText.Length; i++)
        {
            if (i == fullText.Length - 1)
            {
                _button.interactable = true;
            }
            textMeshPro.text += fullText[i];
            yield return new WaitForSeconds(speed);
        }
    }
}