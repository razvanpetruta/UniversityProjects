using UnityEngine;

public class Bullet : MonoBehaviour
{
    private float speed = 20f;
    
    public void InitializeBullet(float playerScale)
    {
        Rigidbody2D rb = GetComponent<Rigidbody2D>();
        rb.velocity = new Vector2(speed * (-playerScale), 0f);
    }
    
    // the bullet is destroyed after 5 seconds
    void Update()
    {
        Destroy(gameObject, 5f); 
    }

    // destroy the bullet when it collisions with an object
    private void OnTriggerEnter2D(Collider2D col)
    {
        if (col.CompareTag("Player") || col.CompareTag("Player2") || col.CompareTag("Map") || col.CompareTag("Objects"))
        {
            Destroy(gameObject);
        }
    }
}