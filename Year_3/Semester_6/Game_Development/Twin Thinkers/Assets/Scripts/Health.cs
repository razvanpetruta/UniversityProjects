using TMPro;
using UnityEngine;
using UnityEngine.UI;
using Random = UnityEngine.Random;

public class Health : MonoBehaviour
{
    [SerializeField] private Slider healthSlider;
    [SerializeField] private GameObject sliderFill;
    [SerializeField] private TextMeshProUGUI healthText;
    [SerializeField] private float maxHealth = 100;
    [SerializeField] private GameObject player;
    
    [SerializeField] private AudioClip[] damageAudio = new AudioClip[19];
    private AudioSource playerAudio;
    
    private float currentHealth;

    private Animator animator;
    private Pickup pickup;
    private Score score;

    private float bulletDamage = 10f;
   
    void Start()
    {
        playerAudio = gameObject.GetComponent<AudioSource>();
        animator = GetComponent<Animator>();
        pickup = GetComponent<Pickup>();
        score = FindObjectOfType<Score>().GetComponent<Score>(); // cauta in scena score si ia componenta
        
        currentHealth = maxHealth;
        healthSlider.value = maxHealth;
        healthText.text = maxHealth.ToString();
    }

    private void UpdateHealthUI()
    {
        healthSlider.value = currentHealth;
        healthText.text = currentHealth.ToString();
    }
    
    // Am facut 2 functii de Hide si Unhide pentru invoke
    private void UnhideGun()
    {
        if (pickup.GetHasWeapon())
        {
            pickup.GetCurrentWeapon().SetActive(true);
        }
    }

    private void HideGun()
    {
        if (pickup.GetHasWeapon())
        {
            pickup.GetCurrentWeapon().SetActive(false);
        }
    }
    
    public void DamagePlayer()
    {
        currentHealth -= bulletDamage;
        UpdateHealthUI();
        
        if (currentHealth <= 0)
        {
            GameObject deadPlayer = this.gameObject;
            
            animator.SetTrigger("Die");
            sliderFill.SetActive(false);
            this.enabled = false;
            deadPlayer.GetComponent<CapsuleCollider2D>().enabled = false;
            deadPlayer.GetComponent<Movement>().enabled = false;
            deadPlayer.GetComponent<Rigidbody2D>().isKinematic = true;
            Destroy(deadPlayer, 1f);
            score.RoundEndAnimation();
            score.StartRound();
            UpdateWin();
        }
        else
        {
            int index = Random.Range(0, 19);
            animator.SetTrigger("Hit");
            playerAudio.clip = damageAudio[index];
            playerAudio.Play(0);
            HideGun();
            Invoke("UnhideGun", 1.2f);
        }
    }

    private void OnTriggerEnter2D(Collider2D col)
    {
        if (col.CompareTag("Bullet"))
        {
            DamagePlayer();
        }
    }
    
    private void UpdateWin()
    {
        if (this.CompareTag("Player"))
        {
            score.Player2Victory();
        }
        else if(this.CompareTag("Player2"))
        {
           score.Player1Victory();
        }
    }
}
