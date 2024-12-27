using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class Stats : MonoBehaviour
{
    [SerializeField] private TextMeshProUGUI statsP1;
    [SerializeField] private TextMeshProUGUI statsP2;
    private int player1Victories;
    private int player2Victories;
    private void Awake()
    {
        player1Victories = PlayerPrefs.GetInt("player1Victories");
        player2Victories = PlayerPrefs.GetInt("player2Victories");
    }

    private void Start()
    {
        statsP1.text = player1Victories.ToString();
        statsP2.text = player2Victories.ToString();
    }
}
