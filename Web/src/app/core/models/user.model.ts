export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
  createdAt: string;
  storeName?: string; // 🔥 Satıcılar için isteğe bağlı olarak ekledik
  balance: number;
}
