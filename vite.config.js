import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: 'globalThis',
  },
  build: {
    outDir: 'src/main/webapp/built',
    rollupOptions: {
      input: 'src/main/js/app.jsx',
      output: {
        entryFileNames: 'bundle.js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name][extname]'
      }
    },
    sourcemap: true
  },
  server: {
    port: 3000,
    open: false
  },
  optimizeDeps: {
    include: ['buffer']
  }
})