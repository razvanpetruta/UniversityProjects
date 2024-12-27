using UnityEngine;

public class Pickup : MonoBehaviour
{
    [SerializeField] GameObject equippedGun = null;
    [SerializeField] private GameObject equippedPistol;
    [SerializeField] Sprite sprite = null;

    private SpriteRenderer playerSprite;
    private GameObject currentWeapon = null;
    
    public bool hasWeapon = false;
    
    private void Start()
    {
        playerSprite = GetComponent<SpriteRenderer>();
    }
    
    private void OnTriggerEnter2D(Collider2D collision)
    {
        if(!hasWeapon && collision.CompareTag("Gun"))
        {
            Destroy(collision.gameObject);
            equippedGun.SetActive(true);
            playerSprite.sprite = sprite;
            hasWeapon = true;
            currentWeapon = equippedGun;
        }
        else if(!hasWeapon && collision.CompareTag("Pistol"))
        {
            Destroy(collision.gameObject);
            equippedPistol.SetActive(true);
            playerSprite.sprite = sprite;
            hasWeapon = true;
            currentWeapon = equippedPistol;
        }
    }

    public bool GetHasWeapon()
    {
        return hasWeapon;
    }

    public GameObject GetCurrentWeapon()
    {
        return currentWeapon;
    }
}
