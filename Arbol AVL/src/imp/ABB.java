package imp;

import api.ABBTDA;

public class ABB implements ABBTDA {

	class NodoABB {
		int info;
		int altura; 
		ABBTDA hijoIzq;
		ABBTDA hijoDer;
	}
	
	NodoABB raiz;
	
	@Override
	public void inicializarArbol() {
		raiz = null;
	}

@Override
public void agregarElem(int x) {
    ABB resultado = agregarYBalancear(this, x);
    this.raiz = resultado.raiz;
}

private ABB agregarYBalancear(ABB a, int x) {
    if (a.raiz == null) {
        a.raiz = new NodoABB();
        a.raiz.info = x;
        a.raiz.altura = 1;
        a.raiz.hijoIzq = new ABB(); a.raiz.hijoIzq.inicializarArbol();
        a.raiz.hijoDer = new ABB(); a.raiz.hijoDer.inicializarArbol();
    } else if (x < a.raiz.info) {
        a.raiz.hijoIzq = agregarYBalancear((ABB) a.raiz.hijoIzq, x);
    } else if (x > a.raiz.info) {
        a.raiz.hijoDer = agregarYBalancear((ABB) a.raiz.hijoDer, x);
    } else {
        // Valor duplicado, no se agrega nada
        return a;
    }
    return a.balancear();
}

	@Override
	public void eliminarElem(int x) {
		if (raiz != null) {
			if (raiz.info == x && raiz.hijoIzq.arbolVacio() && raiz.hijoDer.arbolVacio()) { // Si el elemento es la raiz y no tiene hijos
				// Eliminar la raiz
				raiz = null;

			} else if (raiz.info == x && !raiz.hijoIzq.arbolVacio()) { // Si el elemento es la raiz y tiene hijo izquierdo
				// Reemplazar la raiz por el mayor del subárbol izquierdo
				// eliminar el mayor del subárbol izquierdo
				raiz.info = this.mayor(raiz.hijoIzq);
				raiz.hijoIzq.eliminarElem(raiz.info); // se elimina en un solo recorrido porque es el mayor del subarbol izaquierdo
				
				

			} else if (raiz.info == x && raiz.hijoIzq.arbolVacio()) { // Si el elemento es la raiz y tiene hijo derecho
				// Reemplazar la raiz por el menor del subárbol derecho
				// eliminar el menor del subárbol derecho
				raiz.info = this.menor(raiz.hijoDer);
				raiz.hijoDer.eliminarElem(raiz.info); // se elimina en un solo recorrido porque es el menor del subarbol derecho
				

			} else if (raiz.info < x) { // Si el elemento es mayor que la raiz
				// Buscar en el subárbol derecho
				// eliminar el elemento en el subárbol derecho
				// Si el elemento no está en el subárbol derecho, no hacer nada

				raiz.hijoDer.eliminarElem(x); //se vuelve a ejecutar el método en el hijo derecho

			} else {
				raiz.hijoIzq.eliminarElem(x); //se vuelve a ejecutar el método en el hijo izquierdo
			}
		}
	}

	@Override
	public int raiz() {
		return raiz.info;
	}

	@Override
	public ABBTDA hijoIzq() {
		return raiz.hijoIzq;
	}

	@Override
	public ABBTDA hijoDer() {
		return raiz.hijoDer;
	}

	@Override
	public boolean arbolVacio() {
		return (raiz == null);
	}
	


	private int mayor(ABBTDA a){
	/*
	 Recorre el arbol a traves de sus hijos derechos
	 hasta encontrar el mayor, que es el último nodo
	  */

		if (a.hijoDer().arbolVacio()) // Si no tiene hijo derecho, es el mayor
			return a.raiz();
		else
			return mayor(a.hijoDer());
		}
	
	private int menor(ABBTDA a){
		/*
	 	Recorre el arbol a traves de sus hijos izquierdos
	 	hasta encontrar el menor, que es el último nodo
	  */
		if (a.hijoIzq().arbolVacio()) // Si no tiene hijo izquierdo, es el menor
			return a.raiz();
		else
			return menor(a.hijoIzq());
	}


	/*private ABB rotacionDerecha() {
    ABB nuevaRaiz = (ABB) raiz.hijoIzq; 
	  (ABB) se utiliza para convertir el tipo de nodo de ABBTDA a ABB. 
		convierte el hijo izquierdo en la nueva raíz del árbol
		Este nodo va a ser el nuevo padre del árbol
	

    ABB subarbolCentro = (ABB) nuevaRaiz.raiz.hijoDer; 
	// subarbolCentro reprsenta una referencia al hijo derecho de la nueva raíz.
	// No es un valor, es un puntero

    nuevaRaiz.raiz.hijoDer = this; // el arbol actual se convierte en el hijo derecho de la nueva raíz. Es un puntero.
    this.raiz.hijoIzq = subarbolCentro; // el hijo izquierdo del arbol actual se convierte en el hijo izquierdo de la nueva raíz

    this.actualizarAltura();
    nuevaRaiz.actualizarAltura();

    return nuevaRaiz;
}*/


private ABB rotacionDerecha() {
    ABB nuevaRaiz = (ABB) raiz.hijoIzq;
    ABB subarbolCentro = (ABB) nuevaRaiz.raiz.hijoDer;

    // Crear copia de este árbol
    ABB nuevoDerecho = new ABB();
    nuevoDerecho.raiz = this.raiz;
    nuevoDerecho.raiz.hijoIzq = subarbolCentro;

    nuevaRaiz.raiz.hijoDer = nuevoDerecho;

    nuevoDerecho.actualizarAltura();
    nuevaRaiz.actualizarAltura();

    return nuevaRaiz;
}

private ABB rotacionIzquierda() {
    ABB nuevaRaiz = (ABB) raiz.hijoDer;
    ABB subarbolCentro = (ABB) nuevaRaiz.raiz.hijoIzq;

    // Crear copia de este árbol
    ABB nuevoIzquierdo = new ABB();
    nuevoIzquierdo.raiz = this.raiz;
    nuevoIzquierdo.raiz.hijoDer = subarbolCentro;

    nuevaRaiz.raiz.hijoIzq = nuevoIzquierdo;

    nuevoIzquierdo.actualizarAltura();
    nuevaRaiz.actualizarAltura();

    return nuevaRaiz;
}

private ABB balancear() {
	//A medida que se construye el arbol se va balanceando.
    actualizarAltura();
    int balance = factorBalance();

    if (balance > 1) {
        if (((ABB) raiz.hijoIzq).factorBalance() < 0) { // Chequea dos veces por si hay desbalance doble o simple
		
            raiz.hijoIzq = ((ABB) raiz.hijoIzq).rotacionIzquierda();
        }
        return this.rotacionDerecha();
    }
    if (balance < -1) {
        if (((ABB) raiz.hijoDer).factorBalance() > 0) {
            raiz.hijoDer = ((ABB) raiz.hijoDer).rotacionDerecha();
        }
        return this.rotacionIzquierda();
    }
    return this;
}

//dejo lo de altura para despues
private int altura(ABBTDA arbol) {
    if (arbol.arbolVacio()) return 0;
    return ((ABB) arbol).raiz.altura;
}

private void actualizarAltura() { 
    if (!this.arbolVacio()) {
        raiz.altura = 1 + Math.max(altura(raiz.hijoIzq), altura(raiz.hijoDer));
    }
}

private int factorBalance() { //Chequea si el árbol está balanceado
	
    if (this.arbolVacio()) return 0;
    return altura(raiz.hijoIzq) - altura(raiz.hijoDer);
}

}