using UnityEngine;
using UnityEngine.InputSystem;

public class Movement : MonoBehaviour
{
    [SerializeField] private ParticleSystem jetParticle = null;
    
    [SerializeField] private AudioSource playerAudio;
    [SerializeField] private AudioClip jumpAudio;
    [SerializeField] private AudioClip jetpackAudio;

    [SerializeField] private float moveSpeed = 10f;
    [SerializeField] private float jumpPower;
    [SerializeField] private float jetPower;

    [SerializeField] private Transform groundCheck;
    [SerializeField] private LayerMask groundLayer;
    [SerializeField] private LayerMask objectsLayer;

    private bool isUsingJetpack = false;
    
    private Vector2 moveInput;
    private Rigidbody2D playerRb;
    private Animator animator;
    private Pickup pickup;

    void Start()
    {
        playerRb = GetComponent<Rigidbody2D>();
        animator = GetComponent<Animator>();
        pickup = GetComponent<Pickup>();
    }
    
    void Update()
    {
        Run();
        FlipSprite();
        UpdateAnimator();
    }
    
    private bool IsGrounded()
    {
        return Physics2D.OverlapCircle(groundCheck.position, 0.2f, groundLayer);
    }

    private bool IsOnObjects()
    {
        return Physics2D.OverlapCircle(groundCheck.position, 0.2f, objectsLayer);
    }

    private void UpdateAnimator()
    {
        animator.SetBool("isGrounded", IsGrounded() || IsOnObjects());
    }
    
    private void OnMove(InputValue value)
    {
        moveInput = value.Get<Vector2>();
    }

    private void OnJump(InputValue value)
    {
        if (value.isPressed && (IsGrounded() || IsOnObjects()))
        {
            if (playerAudio != null)
            {
                playerAudio.clip = jumpAudio;
                playerAudio.Play(0);
            }
            animator.SetTrigger("Jump");
            playerRb.velocity += new Vector2(0f, jumpPower);
        }
    }

    private void OnJetpack(InputValue value)
    {
        if (value.isPressed && jetParticle != null)
        {
            if (!isUsingJetpack)
            {
                if (playerAudio != null)
                {
                    playerAudio.loop = true;
                    playerAudio.clip = jetpackAudio;
                    playerAudio.Play(0);
                }
                jetParticle.Play();
                isUsingJetpack = true;
            }
        }
        else
        {
            if (playerAudio != null)
            {
                playerAudio.Stop();
                playerAudio.loop = false;
            }
            jetParticle.Stop();
            isUsingJetpack = false;
        }
    }
    
    private void Run()
    {
        animator.SetFloat("HorizontalMovement",  IsGrounded() || IsOnObjects() ? Mathf.Abs(moveInput.x) : 0f);
        
        Vector2 playerVelocity = new Vector2(moveInput.x * moveSpeed,  isUsingJetpack ? moveInput.y * jetPower : playerRb.velocity.y);
        playerRb.velocity = playerVelocity;
    }

    private void FlipSprite()
    {
        bool playerHasHorizontalSpeed = Mathf.Abs(playerRb.velocity.x) > Mathf.Epsilon;
        if (playerHasHorizontalSpeed)
        {
            transform.localScale = new Vector3(Mathf.Sign(playerRb.velocity.x) * -1f, 1f, 1f);
        }
    }

    public float GetPlayerScale()
    {
        return transform.localScale.x;
    }
}
