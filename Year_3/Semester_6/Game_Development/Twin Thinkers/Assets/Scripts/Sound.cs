using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

public class Sound : MonoBehaviour
{
    [SerializeField] private Sprite soundOn;
    [SerializeField] private Sprite soundOff;
    [SerializeField] private Button button;

    private bool pressedOnce = false;

    public void TurnOffSound()
    {
        pressedOnce = true;
    }
}
