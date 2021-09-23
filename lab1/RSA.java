// Java Program to Implement the RSA Algorithm
import java.math.*;
import java.util.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


class RSA {
	//m ^ e % n
	public static BigInteger encrypt(BigInteger inputMessage, BigInteger e, BigInteger n) {
		return inputMessage.modPow(e, n);
	}

	public static BigInteger decrypt(BigInteger cipherMessage, BigInteger d, BigInteger n) {
		return cipherMessage.modPow(d, n);
	}



	public static void main(String args[]) throws IOException{
		BigInteger p, q, z, e, d, n;
	
        int bitKey = 128;

		//1. We select 2 large prime numbers
		p =  BigInteger.probablePrime(bitKey, new Random());
		q =  BigInteger.probablePrime(bitKey, new Random());
		//2. Calculate n=p*q 
		n = p.multiply(q);
		//3. Calculate phi
		// z = (p - 1)(q - 1)
		z = p.subtract(BigInteger.ONE);
		z = z.multiply(q.subtract(BigInteger.ONE));

		//4. Calculate GCD == 1
		e = BigInteger.probablePrime(bitKey, new Random());
		//Generates random numbers of e until it finds a number that has e%z = 1
		//n has to be larger than e
		//Since e has the same number of bits i can only fit once
		//GCD finds greatest common divider
		while (e.compareTo(z) != 1 || (e.gcd(z).compareTo(BigInteger.ONE) != 0)) {
			e = new BigInteger(z.bitLength(), new Random());

			System.out.println("e = " + e);
		}
		// E is a prime number with same length as phiBig n=p*q
		System.out.println("The public key (e,n) \n");

		//5. Calculate d 
		d = e.modInverse(z);
		System.out.println("the value of d = " + d);

        
		//Test the keys;
		System.out.println("Enter a message to be encrypted: ");
		String messageInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		//prints the Encrypted key
        System.out.println("\nEncrypted key: " + e);

		BigInteger messageCipher = encrypt(new BigInteger(messageInput.getBytes()), e, n);
		System.out.println("\nEncrypted message: " + messageCipher);

		String messageDecrypted = new String(decrypt(messageCipher, d, n).toByteArray());
		System.out.println("Decrypted message: " + messageDecrypted);

	}

}

    
