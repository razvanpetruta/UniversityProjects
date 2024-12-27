using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class Scorer : MonoBehaviour
{
    [SerializeField] private TextMeshProUGUI text;
    private int hits = 0;
    
    private void OnCollisionEnter(Collision other)
    {
        if (!other.gameObject.CompareTag("Hit"))
        {
            hits++;
            text.text = $"Bumped in: {hits} obstacles";
        }
    }
}
