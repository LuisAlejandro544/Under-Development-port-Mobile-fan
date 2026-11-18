/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Componente raíz mínimo ("Hello World").
 * Su único propósito es permitir que el entorno web compile y renderice
 * correctamente mientras el código fuente y los assets de Under Development
 * se obtienen mediante el GitHub Action manual.
 */
export default function App() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-neutral-950 text-neutral-100">
      <h1 className="text-3xl font-semibold tracking-tight">Hello World</h1>
    </main>
  );
}
