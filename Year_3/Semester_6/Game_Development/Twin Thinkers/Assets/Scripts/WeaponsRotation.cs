using UnityEngine;

public class WeaponsRotation : MonoBehaviour
{
    private float rotationSpeed = 120f;
    void Update()
    {
        transform.Rotate(0, rotationSpeed * Time.deltaTime, 0);
    }
}
