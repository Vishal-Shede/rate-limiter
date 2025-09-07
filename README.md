# Token Bucket Rate Limiter (Java)

A minimal **Token Bucket Rate Limiter** implementation in Java.  
It controls how many requests can be processed within a given time window, ensuring fair usage and protecting backend systems.

---

## 🔍 How It Works
- A **bucket** holds tokens (capacity = burst size).  
- Each **request consumes one token**.  
- Tokens are **refilled at a fixed rate** (tokens per second).  
- If the bucket is empty → the request is **rejected** (can be mapped to HTTP 429 in web apps).  

This allows short bursts while still enforcing an average rate.
