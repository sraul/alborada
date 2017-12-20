package com.yhaguy.gestion.contabilidad;

import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.coreweb.control.SimpleViewModel;

public class ContableGeneracionAsientosViewModel extends SimpleViewModel {
	
	private Date desde1 = new Date();
	private Date hasta1 = new Date();
	
	@Wire
	private Window win;

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	
	@Command
	public void generarAsientosVentaCredito() {		
		Clients.showBusy(this.win, "Generando Asientos..");
		Events.echoEvent("onLater", this.win, null);		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void clearProgress() throws Exception {
		Timer timer = new Timer();
		timer.setDelay(1000);
		timer.setRepeats(false);

		timer.addEventListener(Events.ON_TIMER, new EventListener() {
			@Override
			public void onEvent(Event evt) throws Exception {
				Clients.clearBusy(win);
			}
		});
		timer.setParent(this.win);
		ContableControl.generarAsientosVentaCredito(this.desde1, this.hasta1, this.getLoginNombre());
		Clients.showNotification("Asientos Generados..");
	}

	/**
	 * GETS / SETS
	 */
	public Date getDesde1() {
		return desde1;
	}

	public void setDesde1(Date desde1) {
		this.desde1 = desde1;
	}

	public Date getHasta1() {
		return hasta1;
	}

	public void setHasta1(Date desde2) {
		this.hasta1 = desde2;
	}
	
}
