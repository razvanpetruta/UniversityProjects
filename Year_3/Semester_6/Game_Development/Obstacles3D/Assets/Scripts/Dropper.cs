using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Dropper : MonoBehaviour
{
    [SerializeField] private float timeToWait = 5f;
    private MeshRenderer renderer;
    private Rigidbody rigidBody;

    private void Start()
    {
        renderer = GetComponent<MeshRenderer>();
        rigidBody = GetComponent<Rigidbody>();
        
        renderer.enabled = false;
        rigidBody.useGravity = false;
    }

    void Update()
    {
        if (Time.time > timeToWait)
        {
            renderer.enabled = true;
            rigidBody.useGravity = true;
        }
    }
}
