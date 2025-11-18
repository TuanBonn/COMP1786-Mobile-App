import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../helpers/database_helper.dart';
import '../models/observation.dart';

class AddObservationScreen extends StatefulWidget {
  final int hikeId;
  const AddObservationScreen({super.key, required this.hikeId});

  @override
  State<AddObservationScreen> createState() => _AddObservationScreenState();
}

class _AddObservationScreenState extends State<AddObservationScreen> {
  final _formKey = GlobalKey<FormState>();
  final _obsController = TextEditingController();
  final _timeController = TextEditingController();
  final _commentsController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _timeController.text = DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now());
  }

  void _saveObservation() async {
    if (_formKey.currentState!.validate()) {
      Observation newObs = Observation(
        hikeId: widget.hikeId,
        observation: _obsController.text,
        time: _timeController.text,
        comments: _commentsController.text,
      );

      await DatabaseHelper().insertObservation(newObs);
      if (!mounted) return;
      Navigator.pop(context, true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Observation')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _obsController,
                decoration: const InputDecoration(labelText: 'Observation', border: OutlineInputBorder()),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _timeController,
                decoration: const InputDecoration(labelText: 'Time', border: OutlineInputBorder()),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _commentsController,
                decoration: const InputDecoration(labelText: 'Comments (Optional)', border: OutlineInputBorder()),
                maxLines: 2,
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton.icon(
                  onPressed: _saveObservation,
                  icon: const Icon(Icons.save),
                  label: const Text('SAVE OBSERVATION'),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 15),
                    backgroundColor: Colors.green,
                    foregroundColor: Colors.white,
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}