using UnityEngine;
using UnityEngine.InputSystem;

public class Shoot : MonoBehaviour
{
    [SerializeField] GameObject bulletPrefab; 
    [SerializeField] private Transform gunEndPoint; 
    [SerializeField] private GameObject muzzle;

    [SerializeField] private Movement player1;
    [SerializeField] private Movement player2;
    
    [SerializeField] private AudioSource shootAudio;
    
    private void OnFire(InputValue value)
    {
        if (value.isPressed)
        {
            GameObject instantiatedBullet = Instantiate(bulletPrefab, gunEndPoint.position, gunEndPoint.rotation);
            Bullet bullet = instantiatedBullet.GetComponent<Bullet>();
            muzzle.SetActive(true);
            if (shootAudio != null)
            {
                shootAudio.Play(0);
            }
            Invoke("DisableMuzzle", 0.1f);

            if (transform.parent.CompareTag("Player"))
            {
                bullet.InitializeBullet(player1.GetPlayerScale());
            }
            else
            {
                bullet.InitializeBullet(player2.GetPlayerScale());
            }
        }
    }

    private void DisableMuzzle()
    {
        muzzle.SetActive(false);
    }
}