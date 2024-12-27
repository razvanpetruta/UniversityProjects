using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Mover : MonoBehaviour
{
    [SerializeField] private float moveSpeed;
    private float rotationSpeed = 2f;
    private Animator anim;
    private Rigidbody rigidbody;
    private float xInput;
    private float zInput;
    
    void Start()
    {
        rigidbody = gameObject.GetComponent<Rigidbody>();
        anim = gameObject.GetComponent<Animator>();
    }

    void Update()
    {
        PlayerInput();
        CheckChangeState();
        RotationUpdate();
    }

    void FixedUpdate()
    {
        MovePlayer();
    }

    void MovePlayer()
    {
        Vector3 velocity = new Vector3(xInput, 0f, zInput) * moveSpeed;
        rigidbody.velocity = velocity;
    }

    void PlayerInput()
    {
        xInput = Input.GetAxis("Horizontal");
        zInput = Input.GetAxis("Vertical");
        
        if (xInput != 0 || zInput != 0)
        {
            anim.SetBool("Walk_Anim", true);
        }
        else
        {
            anim.SetBool("Walk_Anim", false);
            
        }
    }

    void CheckChangeState()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            if (anim.GetBool("Roll_Anim"))
            {
                anim.SetBool("Roll_Anim", false);
            }
            else
            {
                anim.SetBool("Roll_Anim", true);
            }
        }
    }

    void RotationUpdate()
    {
        Vector3 targetDirection = new Vector3(xInput, 0f, zInput);
        if (targetDirection != Vector3.zero)
        {
            Quaternion targetRotation = Quaternion.LookRotation(targetDirection, Vector3.up);
            transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, rotationSpeed * Time.deltaTime);
        }
    }
}
