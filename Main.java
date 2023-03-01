import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		String key = "";
		FileManager file = new FileManager();
		Scanner scan = new Scanner(System.in);
		HashTableInterface<Long, String> hashTable = new HashTable<Long, String>();

		// File read operations
		file.readFile("file.txt");

		// Hashing and Insertion
		insert(file,hashTable);

		// Search Element
		search(key,scan,hashTable);
		

	}

	private static void insert(FileManager file, HashTableInterface<Long, String> hashTable) {
		Iterator<String> keys = file.getFileIterator();
		while (keys.hasNext()) {
			String value = keys.next();
			long hashedKey = hashTable.hashCode(value.toLowerCase(Locale.ENGLISH));
			hashTable.insert(hashedKey, value);
		}
	}

	private static void search(String key, Scanner scan, HashTableInterface<Long, String> hashTable) {
		System.out.println("------ Hashing Algorithm ------");

		while (true) {
			System.out.print("\nSearch : ");
			key = scan.next();
			long hashedKey = hashTable.hashCode(key.toLowerCase(Locale.ENGLISH));

			if (!hashTable.search(hashedKey)) {
				System.out.println("NOT FOUND !!!");
			}

		}
	}
}
